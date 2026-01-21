package tech.sangdang.invoicer.modules.invoice.app.service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.StartImageUploadInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.ImageUploadAttemptDto;

@Validated
public interface InvoiceProcessingService {
    ImageUploadAttemptDto startImageUploadInvoice(@Valid StartImageUploadInvoiceCommand command);
}
