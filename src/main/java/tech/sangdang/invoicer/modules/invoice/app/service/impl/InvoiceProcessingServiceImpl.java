package tech.sangdang.invoicer.modules.invoice.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetPresignedDownloadUrlQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.StartImageUploadInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.ImageUploadAttemptDto;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.PresignedDownloadUrlDto;
import tech.sangdang.invoicer.modules.invoice.app.service.InvoiceProcessingService;
import tech.sangdang.invoicer.modules.invoice.domain.InvoiceStatus;
import tech.sangdang.invoicer.modules.invoice.domain.error.InvoiceAccessDeniedError;
import tech.sangdang.invoicer.modules.invoice.domain.error.InvoiceCannotBeStartedError;
import tech.sangdang.invoicer.modules.invoice.domain.error.InvoiceNotFoundError;
import tech.sangdang.invoicer.modules.invoice.domain.ports.FileUploadPort;
import tech.sangdang.invoicer.modules.invoice.domain.repository.InvoiceRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvoiceProcessingServiceImpl implements InvoiceProcessingService {
    private final FileUploadPort fileUploadPort;
    private final InvoiceRepository invoiceRepository;

    @Override
    public ImageUploadAttemptDto startImageUploadInvoice(StartImageUploadInvoiceCommand command) {
        var invoice = invoiceRepository.findById(command.getInvoiceId())
                .orElseThrow(() -> new InvoiceNotFoundError("ID", command.getInvoiceId()));

        if(!invoice.getUserId().equals(command.getUserId())) {
            throw new InvoiceAccessDeniedError();
        }
        
        if(!invoice.canBeStarted()) {
            throw new InvoiceCannotBeStartedError(command.getInvoiceId());
        }

        String uploadUrl = fileUploadPort.uploadFile(invoice);

        return ImageUploadAttemptDto.builder()
                .uploadUrl(uploadUrl)
                .build();
    }

    @Override
    public PresignedDownloadUrlDto getPresignedDownloadUrl(GetPresignedDownloadUrlQuery query) {
        var invoice = invoiceRepository.findById(query.getInvoiceId())
                .orElseThrow(() -> new InvoiceNotFoundError("ID", query.getInvoiceId()));

        if(!invoice.getUserId().equals(query.getUserId())) {
            throw new InvoiceAccessDeniedError();
        }
        
        if(!invoice.getStatus().equals(InvoiceStatus.PENDING_VALIDATION.name())) {
            throw new InvoiceCannotBeStartedError(query.getInvoiceId());
        }

        String downloadUrl = fileUploadPort.getDownloadUrl(invoice);

        return PresignedDownloadUrlDto.builder()
                .downloadUrl(downloadUrl)
                .build();
    }
}
