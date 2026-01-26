package tech.sangdang.invoicer.modules.invoice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tech.sangdang.invoicer.common.constants.AppSecurity;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.CreateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.UpdateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;

import java.util.List;

@SecurityRequirement(name = AppSecurity.CLIENT_CREDENTIALS)
@Tag(name = "Invoice")
public interface InvoiceInternalController {
    String PATH = "/internal/invoice";

    @PreAuthorize("hasAnyAuthority(@S.INVOICE_CREATE, @S.INVOICE_WRITE_OWNED)")
    @Operation(summary = "[internal] Create an invoice")
    @PostMapping()
    default ResponseEntity<InvoiceResponseDto> createInvoice(@RequestBody CreateInvoiceCommand command, @AuthenticationPrincipal Jwt principal) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PreAuthorize("hasAnyAuthority(@S.INVOICE_UPDATE_OWNED, @S.INVOICE_WRITE_OWNED)")
    @Operation(summary = "[internal] Update an invoice")
    @PatchMapping("/{invoiceId}")
    default InvoiceResponseDto updateInvoice(@PathVariable String invoiceId, @RequestBody UpdateInvoiceCommand command) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PreAuthorize("hasAnyAuthority(@S.INVOICE_DELETE_OWNED, @S.INVOICE_WRITE_OWNED)")
    @Operation(summary = "[internal] Delete an invoice")
    @DeleteMapping("/{invoiceId}")
    default ResponseEntity<Void> deleteInvoice(@PathVariable String invoiceId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PreAuthorize("hasAnyAuthority(@S.INVOICE_READ_OWNED)")
    @Operation(summary = "[internal] Get my created invoices")
    @GetMapping("/batch")
    default List<InvoiceResponseDto> getOwnedInvoices() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PreAuthorize("hasAnyAuthority(@S.INVOICE_READ_OWNED)")
    @Operation(summary = "[internal] Get my created invoice by ID")
    @GetMapping("/{invoiceId}")
    default InvoiceResponseDto getOwnedInvoiceById(@PathVariable String invoiceId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
