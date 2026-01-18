package tech.sangdang.invoicer.modules.invoice.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sangdang.invoicer.modules.invoice.api.InvoiceUserController;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetAllInvoicesQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetInvoiceByIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.StartImageUploadInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.ImageUploadAttemptDto;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;
import tech.sangdang.invoicer.modules.invoice.app.service.InvoiceProcessingService;
import tech.sangdang.invoicer.modules.invoice.app.service.InvoiceQueryService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(InvoiceUserController.PATH)
public class InvoiceUserControllerImpl implements InvoiceUserController {
    private InvoiceQueryService invoiceQueryService;
    private InvoiceProcessingService invoiceProcessingService;

    @Override
    public List<InvoiceResponseDto> getAllInvoices() {
        return invoiceQueryService.getAllInvoices(GetAllInvoicesQuery.builder().build());
    }

    @Override
    public InvoiceResponseDto getInvoiceById(String invoiceId) {
        return invoiceQueryService.getInvoiceById(GetInvoiceByIdQuery.builder().invoiceId(invoiceId).build());
    }

    @Override
    public ImageUploadAttemptDto startProcessingInvoice(String invoiceId, StartImageUploadInvoiceCommand command) {
        return invoiceProcessingService.startImageUploadInvoice(command.toBuilder().invoiceId(invoiceId).build());
    }
}
