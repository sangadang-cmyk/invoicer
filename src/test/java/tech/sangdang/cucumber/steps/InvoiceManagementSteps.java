package tech.sangdang.cucumber.steps;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tech.sangdang.cucumber.CucumberStepParent;
import tech.sangdang.cucumber.ScenarioContext;
import tech.sangdang.invoicer.common.core.ErrorResponse;
import tech.sangdang.invoicer.modules.account.domain.error.UserNotFoundError;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.CreateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.UpdateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;
import tech.sangdang.invoicer.modules.invoice.domain.InvoiceAllowedTypes;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
public class InvoiceManagementSteps extends CucumberStepParent {
    @Autowired
    private ScenarioContext context;
    @Autowired
    private MockMvc mockMvc;

    @Given("I have created an invoice with description {string}, size {int} bytes, and allowing {string} mime types")
    @When("I create an invoice with description {string}, size {int} bytes, and allowing {string} mime types")
    public void iCreateAnInvoiceWithDescriptionSizeBytesAndAllowingMimeTypes(String description, Integer maxSize, String allowedTypes) throws Exception {
        String userId = context.getData("userId");
        
        log.debug("Creating invoice with description: {}, maxSize: {}, allowedTypes: {}; for user {}", description, maxSize, allowedTypes, userId);

        CreateInvoiceCommand command = CreateInvoiceCommand.builder()
                .description(description)
                .maxSizeInBytes(maxSize)
                .allowedTypes(Arrays.stream(allowedTypes.split(","))
                        .map(InvoiceAllowedTypes::fromString)
                        .toList())
                .userId(userId)
                .build();

        var response = mockMvc.perform(ensureAuth(
                post("/api/internal/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(super.objectMapper.writeValueAsString(command))
                , context.getLoggedInSession()
        )).andReturn();

        context.addApiResult(response);
        context.putData("CreateInvoiceCommand", command);
        context.putData("CreatedInvoice", super.parseBodyFromResponse(response, InvoiceResponseDto.class));
    }

    @When("I update the invoice to have description {string}, size {int} bytes, and allowing {string} mime types")
    public void iUpdateTheInvoiceToHaveDescriptionSizeBytesAndAllowingMimeTypes(String description, Integer maxSize, String allowedTypes) throws Exception {
        UpdateInvoiceCommand command = UpdateInvoiceCommand.builder()
                .description(description)
                .maxSizeInBytes(maxSize)
                .allowedTypes(Arrays.stream(allowedTypes.split(","))
                        .map(InvoiceAllowedTypes::fromString)
                        .collect(Collectors.toSet()))
                .build();

        var createInvoice_invoiceId = ((InvoiceResponseDto) context.getData("CreatedInvoice")).getInvoiceId();
        assertNotNull("Invoice ID must not be null", createInvoice_invoiceId);

        var response = mockMvc.perform(ensureAuth(
                patch("/api/internal/invoice/" + createInvoice_invoiceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(super.objectMapper.writeValueAsString(command))
                , context.getLoggedInSession()
        )).andReturn();

        context.addApiResult(response);
        context.putData("UpdateInvoiceCommand", command);
        context.putData("UpdatedInvoice", super.parseBodyFromResponse(response, InvoiceResponseDto.class));
    }

    @When("I delete the invoice")
    public void i_delete_the_invoice() throws Exception {
        var createInvoice_invoiceId = ((InvoiceResponseDto) context.getData("CreatedInvoice")).getInvoiceId();
        assertNotNull("Invoice ID must not be null", createInvoice_invoiceId);
        
        var response = mockMvc.perform(ensureAuth(
                delete("/api/internal/invoice/" + createInvoice_invoiceId)
                , context.getLoggedInSession()
        )).andReturn();
        
        context.addApiResult(response);
    }

    @Then("The invoice should be created successfully")
    public void theInvoiceShouldBeCreatedSuccessfully() {
        var result = context.getLatestApiResult();
        var responseBody = ((InvoiceResponseDto) context.getData("CreatedInvoice"));
        var createInvoiceCommand = (CreateInvoiceCommand) context.getData("CreateInvoiceCommand");
        assertEquals("Status should be 201", 201, result.getResponse().getStatus());

        assertEquals("Description must be same", createInvoiceCommand.getDescription(), responseBody.getDescription());
        assertEquals("Max size must be same", createInvoiceCommand.getMaxSizeInBytes(), responseBody.getMaxSizeInBytes());
    }

    @Then("The invoice should be updated successfully")
    public void theInvoiceShouldBeUpdatedSuccessfully() {
        var result = context.getLatestApiResult();
        var responseBody = ((InvoiceResponseDto) context.getData("UpdatedInvoice"));
        var updateInvoiceCommand = (UpdateInvoiceCommand) context.getData("UpdateInvoiceCommand");
        assertEquals("Status should be 200", 200, result.getResponse().getStatus());

        assertEquals("Description must be same", updateInvoiceCommand.getDescription(), responseBody.getDescription());
        assertEquals("Max size must be same", updateInvoiceCommand.getMaxSizeInBytes(), responseBody.getMaxSizeInBytes());
    }
    
    @Then("The invoice should be deleted successfully")
    public void theInvoiceShouldBeDeletedSuccessfully() {
        var result = context.getLatestApiResult();
        assertEquals("Status should be 204", 204, result.getResponse().getStatus());
    }
    
    @Then("attempting to retrieve the invoice should result in a not found error")
    public void attemptingToRetrieveTheInvoiceShouldResultInANotFoundError() throws Exception {
        var createInvoice_invoiceId = ((InvoiceResponseDto) context.getData("CreatedInvoice")).getInvoiceId();
        
        assertNotNull("Invoice ID must not be null", createInvoice_invoiceId);
        
        var response = mockMvc.perform(ensureAuth(
                get("/api/internal/invoice/" + createInvoice_invoiceId)
                , context.getLoggedInSession()
        )).andReturn();
        
        assertEquals("Status should be 404", 404, response.getResponse().getStatus());
    }

    @Then("The invoice creation should fail with a User Not Found error")
    public void theInvoiceCreationShouldFailWithAnInvalidUserIDError() throws UnsupportedEncodingException {
        var result = context.getLatestApiResult();
        var sampleError = new UserNotFoundError(null, null);
        assertEquals("Status should be 400", sampleError.getHttpStatus().value(), result.getResponse().getStatus());
        
        var responseBody = super.parseBodyFromResponse(result, ErrorResponse.class);
        assertEquals("Error code should be valid", sampleError.getErrorCode(), responseBody.getError());
    }
}
