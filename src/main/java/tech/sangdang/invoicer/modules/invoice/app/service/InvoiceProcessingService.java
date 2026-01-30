package tech.sangdang.invoicer.modules.invoice.app.service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetPresignedDownloadUrlQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.StartImageUploadInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.ImageUploadAttemptDto;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.PresignedDownloadUrlDto;

@Validated
public interface InvoiceProcessingService {
    ImageUploadAttemptDto startImageUploadInvoice(@Valid StartImageUploadInvoiceCommand command);
    PresignedDownloadUrlDto getPresignedDownloadUrl(@Valid GetPresignedDownloadUrlQuery query);
}
