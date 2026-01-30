import { S3Client, CopyObjectCommand, DeleteObjectCommand, HeadObjectCommand } from "@aws-sdk/client-s3";
import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient, UpdateCommand, GetCommand } from "@aws-sdk/lib-dynamodb";

const client = new DynamoDBClient({});
const docClient = DynamoDBDocumentClient.from(client);
const s3 = new S3Client({});

export const handler = async (event) => {
    const sourceBucket = event.Records[0].s3.bucket.name;
    const key = event.Records[0].s3.object.key;
    console.log("Received upload for: " + sourceBucket + " at key: " + key);

    const destBucket = "invoicer-permastore"

    try {
        // Fetch invoice from DynamoDB
        console.log("Fetching invoice from DynamoDB for invoiceId:", key);
        const getInvoiceCommand = await docClient.send(new GetCommand({
            TableName: "invoice",
            Key: {
                invoiceId: key,
            }
        }));
        
        const invoice = getInvoiceCommand.Item;
        console.log("Invoice retrieved. Status:", invoice?.status, "AllowedTypes:", invoice?.allowedTypes);
        
        // If invoice not found or not in AWAITING_UPLOAD status, delete file and exit
        if (!invoice) {
            console.warn("Invoice not found for invoiceId:", key, "- Deleting uploaded file from", sourceBucket);
            await s3.send(new DeleteObjectCommand({
                Bucket: sourceBucket,
                Key: key,
            }));
            console.log("File deleted successfully from", sourceBucket);
            return { status: "Success" }; // good flow, no errors here
        }
        
        if (invoice.status !== "AWAITING_UPLOAD") {
            console.warn("Invoice status is", invoice.status, "(expected AWAITING_UPLOAD) - Deleting uploaded file from", sourceBucket);
            await s3.send(new DeleteObjectCommand({
                Bucket: sourceBucket,
                Key: key,
            }));
            console.log("File deleted successfully from", sourceBucket);
            return { status: "Success" }; // good flow, no errors here
        }
        
        // Get file metadata to check content type
        console.log("Retrieving file metadata from S3 for content type validation");
        const headObjectResponse = await s3.send(new HeadObjectCommand({
            Bucket: sourceBucket,
            Key: key,
        }));
        
        const rawContentType = headObjectResponse.ContentType?.toLowerCase();
        console.log("File ContentType:", rawContentType);

        if(!rawContentType) {
            console.error("ContentType metadata is missing from the uploaded file");
            throw new Error("ContentType metadata is missing from the uploaded file.");
        }
        
        // Extract MIME type (strip charset and other parameters)
        const contentType = rawContentType.split(';')[0].trim();
        console.log("Parsed MIME type:", contentType);
        
        // Convert allowedTypes array to Set and validate
        const allowedTypesSet = new Set(invoice.allowedTypes);
        console.log("Validating file type. ContentType:", contentType, "AllowedTypes:", Array.from(allowedTypesSet));
        
        if (!allowedTypesSet.has(contentType)) {
            const timestamp = new Date().toISOString();
            const errorMessage = `[${timestamp}] File type '${contentType}' not allowed.`;
            
            console.warn("File type validation failed:", errorMessage);
            
            // Update invoice status to REJECTED and append error log
            console.log("Updating invoice status to REJECTED and appending error log");
            await docClient.send(new UpdateCommand({
                TableName: "invoice",
                Key: {
                    invoiceId: key,
                },
                UpdateExpression: "SET #status = :rejected, errorLogs = list_append(if_not_exists(errorLogs, :emptyList), :newError), updatedAt = :updatedAt",
                ExpressionAttributeNames: {
                    "#status": "status"
                },
                ExpressionAttributeValues: {
                    ":rejected": "REJECTED",
                    ":emptyList": [],
                    ":newError": [errorMessage],
                    ":updatedAt": timestamp
                },
                ReturnValues: "ALL_NEW"
            }));
            console.log("Invoice status updated to REJECTED");
            
            // Delete file from invoicer-inbound
            console.log("Deleting rejected file from", sourceBucket);
            await s3.send(new DeleteObjectCommand({
                Bucket: sourceBucket,
                Key: key,
            }));
            console.log("Rejected file deleted successfully");
            
            return { status: "Success" }; // good flow, no errors here
        }
        
        console.log("File type validation successful. Proceeding with file processing");
        
        // save object to permastore
        console.log("Copying object from:", sourceBucket, "to:", destBucket);
        await s3.send(new CopyObjectCommand({
            Bucket: destBucket,
            CopySource: `${sourceBucket}/${key}`,
            Key: key,
        }));
        console.log("File copied successfully to permastore");

        // update dynamodb row status
        console.log("Updating invoice status to PENDING_VALIDATION");
        const updateCommand = await docClient.send(new UpdateCommand({
            TableName: "invoice",
            Key: {
                invoiceId: key,
            },
            UpdateExpression: "SET #s = :newStatus, updatedAt = :updatedAt",
            ExpressionAttributeNames: {
                "#s": "status"
            },
            ExpressionAttributeValues: {
                ":newStatus": "PENDING_VALIDATION",
                ":updatedAt": new Date().toISOString()
            },
            ReturnValues: "ALL_NEW"
        }))
        console.log("Invoice status updated to PENDING_VALIDATION. Updated values:", updateCommand.Attributes);

        // delete old object
        console.log("Deleting original file from:", sourceBucket);
        await s3.send(new DeleteObjectCommand({
            Bucket: sourceBucket,
            Key: key,
        }));
        console.log("Original file deleted successfully from", sourceBucket);
        console.log("Invoice processing completed successfully for invoiceId:", key);

        return { status: "Success" };
    } catch (err) {
        console.error("Error processing invoice upload for invoiceId:", key, "Error:", err);
        throw err;
    }
};
