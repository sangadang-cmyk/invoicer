package tech.sangdang.cucumber.steps;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import io.awspring.cloud.s3.S3Template;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import tech.sangdang.cucumber.CucumberStepParent;
import tech.sangdang.cucumber.ScenarioContext;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.StartImageUploadInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.ImageUploadAttemptDto;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;
import tech.sangdang.invoicer.modules.invoice.domain.InvoiceStatus;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@Slf4j
public class InvoiceProcessingSteps extends CucumberStepParent {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ScenarioContext context;
    @Autowired
    S3Template s3Template;

    @Given("I have a sample file {string}")
    public void iHaveASampleFileSample_invoicePdfWithSize10000Bytes(String fileName) throws IOException {
        Path resourcePath = Paths.get("src", "test", "resources", "samples", fileName);
        
        Path tempFile = Files.createTempFile("cucumber_", "_" + fileName);
        Files.copy(resourcePath, tempFile, StandardCopyOption.REPLACE_EXISTING);
        
        context.putData("sampleFilePath", tempFile.toAbsolutePath().toString());
        context.putData("sampleFileName", fileName);
        
        tempFile.toFile().deleteOnExit();
        
        log.debug("Sample file copied to temporary location: {}", tempFile.toAbsolutePath().toString());
    }

    @When("I start processing the invoice I created before")
    public void iStartProcessingTheInvoiceICreatedBefore() throws Exception {
        var invoiceCreationResponse = context.getLatestApiResult();
        var invoiceId = objectMapper
                .readValue(
                        invoiceCreationResponse.getResponse().getContentAsString(),
                        InvoiceResponseDto.class
                )
                .getInvoiceId();
        
        log.debug("Starting invoice processing for invoice {}", invoiceId);

        StartImageUploadInvoiceCommand command = StartImageUploadInvoiceCommand.builder()
                .build();

        var response = mockMvc.perform(ensureAuth(
                put("/api/user/invoice/{invoiceId}/start", invoiceId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(command)),
                context.getLoggedInSession()
        )).andReturn();

        context.addApiResult(response);
    }

    @Then("I should receive an S3 presigned URL")
    public void iShouldReceiveAnS3PresignedURL() throws UnsupportedEncodingException {
        var result = context.getLatestApiResult();
        assertEquals(
                "Expected HTTP status 200 OK",
                200,
                result.getResponse().getStatus()
        );

        var responseBody = super.parseBodyFromResponse(result, ImageUploadAttemptDto.class);

        assertNotNull("Response should not be null", responseBody);
        assertNotNull("Upload URL should not be null", responseBody.getUploadUrl());
        log.debug("Received presigned URL: {}", responseBody.getUploadUrl());

        context.putData("presignedUploadUrl", responseBody.getUploadUrl());
    }

    @When("I upload the sample file to the presigned URL")
    public void iUploadTheSampleFileToThePresignedURL() throws Exception {
        String sampleFilePath = context.getData("sampleFilePath");
        String uploadUrl = context.getData("presignedUploadUrl");

        File fileToUpload = new File(sampleFilePath);
        assertTrue("File exists", fileToUpload.exists());
        
        log.debug("Uploading file {} to URL {}", sampleFilePath, uploadUrl);

        var response = RestAssured
                .given()
                    .urlEncodingEnabled(false) // rest assured encodes URL by default. if this is enabled, the url is encoded twice :(
                    .header("Content-Type", "application/pdf")
                    .header("Host", "localhost.localstack.cloud")
                    .body(fileToUpload)
                    .log().all()
                .when()
                    .put(uploadUrl)
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                .extract().response();
        
        log.debug("File upload response: {}", response);
        
        var objects = s3Template.listObjects("invoicer-inbound", "");
        log.debug("Current objects in inbound bucket: {}", objects);
    }
    
    @Then("The invoice status should be updated to PENDING_VALIDATION within {int} seconds")
    public void theInvoiceStatusShouldBeUpdatedToPENDING_VALIDATION(int seconds) throws Exception {
        var invoiceCreationResponse = context.getOldestApiResult();
        var invoiceId = objectMapper
                .readValue(
                        invoiceCreationResponse.getResponse().getContentAsString(),
                        InvoiceResponseDto.class
                )
                .getInvoiceId();
        
        assertNotNull("Invoice ID should not be null", invoiceId);
        
        // for some reason, context is not accessible within the await block, so save it here
        var loggedInSession = context.getLoggedInSession();

        await()
                .atMost(seconds, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    log.debug("Checking status of invoice {}", invoiceId);

                    var response = mockMvc.perform(ensureAuth(
                            get("/api/user/invoice/{invoiceId}", invoiceId),
                            loggedInSession
                    )).andReturn();
                    
                    assertEquals("Expected HTTP status 200 OK", 200, response.getResponse().getStatus());

                    var invoiceDetails = super.parseBodyFromResponse(response, InvoiceResponseDto.class);
                    
                    if(invoiceDetails.getStatus().equals(InvoiceStatus.AWAITING_UPLOAD.name())) {
                        log.debug("Invoice {} is still in AWAITING_UPLOAD status", invoiceId);
                        return false;
                    }

                    assertEquals("Invoice status should be PENDING_VALIDATION", InvoiceStatus.PENDING_VALIDATION.name(), invoiceDetails.getStatus());
                    return true;
                });
    }

    @And("There should exist a new item in the processed bucket")
    public void thereShouldExistANewItemInTheProcessedBucket() throws Exception {
        var invoiceCreationResponse = context.getOldestApiResult();
        var invoiceId = objectMapper
                .readValue(
                        invoiceCreationResponse.getResponse().getContentAsString(),
                        InvoiceResponseDto.class
                )
                .getInvoiceId();
        
        var existsObject = s3Template.objectExists("invoicer-permastore", invoiceId);
        
        assertTrue("Processed S3 object should exist for invoice " + invoiceId, existsObject);
    }
}
