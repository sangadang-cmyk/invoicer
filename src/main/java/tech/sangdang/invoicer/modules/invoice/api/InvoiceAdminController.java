package tech.sangdang.invoicer.modules.invoice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tech.sangdang.invoicer.common.constants.AppSecurity;
import tech.sangdang.invoicer.config.OpenAPIConfig;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.CreateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.UpdateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;

import java.util.List;

@SecurityRequirement(name = AppSecurity.OAUTH2)
@Tag(name = "Invoice")
public interface InvoiceAdminController {
    String PATH = "/admin/invoice";

    @Operation(summary = "Create an invoice")
    @PostMapping()
    default ResponseEntity<InvoiceResponseDto> createInvoice(@RequestBody CreateInvoiceCommand command, @AuthenticationPrincipal Jwt principal) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Operation(summary = "Update an invoice")
    @PatchMapping("/{invoiceId}")
    default InvoiceResponseDto updateInvoice(@PathVariable String invoiceId, @RequestBody UpdateInvoiceCommand command) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Operation(summary = "Delete an invoice")
    @DeleteMapping("/{invoiceId}")
    default ResponseEntity<Void> deleteInvoice(@PathVariable String invoiceId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Operation(summary = "Get my invoices")
    @GetMapping("/batch")
    default List<InvoiceResponseDto> getAllInvoices() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Operation(summary = "Get my invoice by ID")
    @GetMapping("/{invoiceId}")
    default InvoiceResponseDto getInvoiceById(@PathVariable String invoiceId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
