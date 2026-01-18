package tech.sangdang.invoicer.modules.invoice.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.StartImageUploadInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.ImageUploadAttemptDto;
import tech.sangdang.invoicer.modules.invoice.app.service.InvoiceProcessingService;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        if(!invoice.getUserId().equals(command.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to upload image for this invoice");
        }

        String uploadUrl = fileUploadPort.uploadFile(invoice);

        return ImageUploadAttemptDto.builder()
                .uploadUrl(uploadUrl)
                .build();
    }
}
