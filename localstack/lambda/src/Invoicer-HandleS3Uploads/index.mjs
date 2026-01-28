import { S3Client, CopyObjectCommand, DeleteObjectCommand } from "@aws-sdk/client-s3";
import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient, UpdateCommand } from "@aws-sdk/lib-dynamodb";

const client = new DynamoDBClient({});
const docClient = DynamoDBDocumentClient.from(client);
const s3 = new S3Client({});

export const handler = async (event) => {
    const sourceBucket = event.Records[0].s3.bucket.name;
    const key = event.Records[0].s3.object.key;
    console.log("Received upload for: " + sourceBucket + " at key: " + key);

    const destBucket = "invoicer-permastore"

    try {
        // save object to permastore
        console.debug("Copying object from: " + sourceBucket + " to: " + destBucket);
        await s3.send(new CopyObjectCommand({
            Bucket: destBucket,
            CopySource: `${sourceBucket}/${key}`,
            Key: key,
        }));

        // update dynamodb row status
        const updateCommand = await docClient.send(new UpdateCommand({
            TableName: "invoice",
            Key: {
                invoiceId: key,
            },
            UpdateExpression: "SET #s = :newStatus",
            ExpressionAttributeNames: {
                "#s": "status"
            },
            ExpressionAttributeValues: {
                ":newStatus": "PENDING_VALIDATION"
            },
            ReturnValues: "ALL_NEW"
        }))
        console.log("Updated values:", updateCommand);

        // delete old object
        console.debug("Deleting object from: " + sourceBucket);
        await s3.send(new DeleteObjectCommand({
            Bucket: sourceBucket,
            Key: key,
        }));

        return { status: "Success" };
    } catch (err) {
        console.error(err);
        throw err;
    }
};
