package tech.sangdang.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import tech.sangdang.cucumber.CucumberSpringParent;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.CreateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.DeleteInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetInvoiceByIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.UpdateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;
import tech.sangdang.invoicer.modules.invoice.app.service.InvoiceManagementService;
import tech.sangdang.invoicer.modules.invoice.app.service.InvoiceQueryService;
import tech.sangdang.invoicer.modules.invoice.domain.InvoiceAllowedTypes;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceSteps extends CucumberSpringParent {
    @Autowired
    private InvoiceQueryService invoiceQueryService;

    @Autowired
    private InvoiceManagementService invoiceManagementService;

    private InvoiceResponseDto lastCreatedInvoice;

    @Given("I have logged in as internal")
    public void i_have_logged_in_as_internal() {
        var userId = UUID.randomUUID().toString();
    }

    @When("I submit a new invoice with {string}, {string}, {int}, and {string}")
    public void i_submit_a_new_invoice(String description, String userId, Integer maxSizeInBytes, String allowedTypes) {
        var createdByUserId = UUID.randomUUID().toString();
        var invoice = invoiceManagementService.createInvoice(CreateInvoiceCommand.builder()
                .userId(userId)
                .description(description)
                .allowedTypes(List.of(InvoiceAllowedTypes.fromString(allowedTypes)))
                .maxSizeInBytes(maxSizeInBytes)
                .createdByUserId(createdByUserId)
                .build());

        assertNotNull(invoice, "Invoice should not be null");
        assertNotNull(invoice.getInvoiceId(), "Invoice ID should not be null");
        assertEquals(description, invoice.getDescription(), "Description should match");
        assertEquals(userId, invoice.getUserId(), "User ID should match");
        assertEquals(maxSizeInBytes, invoice.getMaxSizeInBytes(), "Max size should match");
        assertEquals(createdByUserId, invoice.getCreatedByUserId(), "Created by user ID should match");

        this.lastCreatedInvoice = invoice;
    }

    @Then("I should see the invoice created")
    public void i_should_see_the_invoice_created() {
        var invoice = invoiceQueryService.getInvoiceById(GetInvoiceByIdQuery.builder()
                .invoiceId(this.lastCreatedInvoice.getInvoiceId()).build());

        assertNotNull(invoice, "Invoice should not be null");
    }

    @When("I update the invoice with new {string} and {int}")
    public void i_update_the_invoice(String newDescription, Integer newMaxSizeInBytes) {
        var invoice = this.invoiceManagementService.updateInvoice(UpdateInvoiceCommand.builder()
                .invoiceId(lastCreatedInvoice.getInvoiceId())
                .description(newDescription)
                .maxSizeInBytes(newMaxSizeInBytes)
                .build());

        assertNotNull(invoice, "Invoice should not be null");
        assertEquals(newDescription, invoice.getDescription(), "Description should match");
        assertEquals(newMaxSizeInBytes, invoice.getMaxSizeInBytes(), "Max size should match");
    }

    @Then("I should see the invoice updated with new {string} and {int}")
    public void i_should_see_the_invoice_updated(String expectedDescription, Integer expectedMaxSizeInBytes) {
        var invoice = invoiceQueryService.getInvoiceById(GetInvoiceByIdQuery.builder()
                .invoiceId(lastCreatedInvoice.getInvoiceId())
                .build());

        assertNotNull(invoice, "Invoice should not be null");
        assertEquals(expectedDescription, invoice.getDescription(), "Description should match");
        assertEquals(expectedMaxSizeInBytes, invoice.getMaxSizeInBytes(), "Max size should match");
    }

    @When("I delete the invoice")
    public void i_delete_the_invoice() {
        this.invoiceManagementService.deleteInvoice(DeleteInvoiceCommand.builder()
                .invoiceId(this.lastCreatedInvoice.getInvoiceId())
                .build());
    }

    @Then("I should not see the invoice in the system")
    public void i_should_not_see_the_invoice_in_the_system() {
        assertThrows(ResponseStatusException.class, () -> invoiceQueryService.getInvoiceById(GetInvoiceByIdQuery.builder()
                .invoiceId(this.lastCreatedInvoice.getInvoiceId())
                .build()));
    }
}