package tech.sangdang.invoicer.modules.invoice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.StartImageUploadInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.ImageUploadAttemptDto;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;

import java.util.List;

@Tag(name = "Invoice")
public interface InvoiceUserController {
    String PATH = "/user/invoice";

    @Operation(summary = "Get my invoices")
    @GetMapping("/batch")
    List<InvoiceResponseDto> getAllInvoices();

    @Operation(summary = "Get my invoice by ID")
    @GetMapping("/{invoiceId}")
    InvoiceResponseDto getInvoiceById(@PathVariable String invoiceId);

    @Operation(summary = "Start processing an invoice")
    @PutMapping("/{invoiceId}/start")
    ImageUploadAttemptDto startProcessingInvoice(@PathVariable String invoiceId, @RequestBody StartImageUploadInvoiceCommand command);
}
