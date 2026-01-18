package tech.sangdang.invoicer.modules.invoice.app.service;

import tech.sangdang.invoicer.modules.invoice.app.dto.req.StartImageUploadInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.ImageUploadAttemptDto;

public interface InvoiceProcessingService {

    ImageUploadAttemptDto startImageUploadInvoice(StartImageUploadInvoiceCommand command);

}
