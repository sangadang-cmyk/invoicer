package tech.sangdang.invoicer.modules.invoice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tech.sangdang.invoicer.common.constants.AppSecurity;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.StartImageUploadInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.ImageUploadAttemptDto;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.PresignedDownloadUrlDto;

import java.util.List;

@SecurityRequirement(name = AppSecurity.AUTH_CODE)
@Tag(name = "Invoice")
public interface InvoiceUserController {
    String PATH = "/user/invoice";

    @Operation(summary = "[user] Get my invoices")
    @GetMapping("/batch")
    List<InvoiceResponseDto> getAllInvoices(@AuthenticationPrincipal Jwt principal);

    @Operation(summary = "[user] Get my invoice by ID")
    @GetMapping("/{invoiceId}")
    InvoiceResponseDto getInvoiceById(@PathVariable String invoiceId, @AuthenticationPrincipal Jwt principal);

    @Operation(summary = "[user] Start processing an invoice")
    @PutMapping("/{invoiceId}/start")
    ImageUploadAttemptDto startProcessingInvoice(@PathVariable String invoiceId, @RequestBody StartImageUploadInvoiceCommand command, @AuthenticationPrincipal Jwt principal);
    
    @Operation(summary = "[user] Get presigned download URL for an invoice")
    @GetMapping("/{invoiceId}/download")
    PresignedDownloadUrlDto getPresignedDownloadUrl(@PathVariable String invoiceId, @AuthenticationPrincipal Jwt principal);
}
