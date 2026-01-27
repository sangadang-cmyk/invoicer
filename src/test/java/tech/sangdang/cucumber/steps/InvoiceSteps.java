package tech.sangdang.cucumber.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tech.sangdang.cucumber.CucumberStepParent;
import tech.sangdang.cucumber.ScenarioContext;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.CreateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.domain.Invoice;
import tech.sangdang.invoicer.modules.invoice.domain.InvoiceAllowedTypes;
import tools.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Slf4j
public class InvoiceSteps extends CucumberStepParent {
    @Autowired
    private ScenarioContext context;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @When("I create an invoice with description {string}, size {int} bytes, and allowing {string} mime types")
    public void iCreateAnInvoiceWithDescriptionSizeBytesAndAllowingMimeTypes(String description, String maxSize, String allowedTypes) throws Exception {
        CreateInvoiceCommand command = CreateInvoiceCommand.builder()
                .description(description)
                .maxSizeInBytes(Integer.parseInt(maxSize))
                .allowedTypes(Arrays.stream(allowedTypes.split(","))
                        .map(InvoiceAllowedTypes::fromString)
                        .toList())
                .userId(context.getData("userId"))
                .build();

        var response = mockMvc.perform(ensureAuth(
                post("/api/internal/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                , context.getLoggedInSession()
        )).andReturn();

        context.addApiResult(response);
        context.putData("CreateInvoiceCommand", command);
    }

    @Then("The invoice should be created successfully")
    public void theInvoiceShouldBeCreatedSuccessfully() throws UnsupportedEncodingException {
        var result = context.getLatestApiResult();
        int status = result.getResponse().getStatus();
        var rawResponseBody = result.getResponse().getContentAsString();
        var responseBody = objectMapper.readValue(rawResponseBody, Invoice.class);
        var createInvoiceCommand = (CreateInvoiceCommand) context.getData("CreateInvoiceCommand");
        assertEquals("Status should be 201", 201, status);
        
        assertEquals("Description must be same", createInvoiceCommand.getDescription(), responseBody.getDescription());
        assertEquals("Max size must be same", createInvoiceCommand.getMaxSizeInBytes(), responseBody.getMaxSizeInBytes());
        assertEquals("Allowed types must be same", createInvoiceCommand.getAllowedTypes(), responseBody.getAllowedTypes());
    }
}
