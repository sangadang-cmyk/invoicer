package tech.sangdang.invoicer.modules.invoice.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sangdang.invoicer.modules.invoice.api.InvoiceUserController;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetAllInvoicesByUserIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetInvoiceByIdAndUserIdQuery;
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
    private final InvoiceQueryService invoiceQueryService;
    private final InvoiceProcessingService invoiceProcessingService;

    @Override
    public List<InvoiceResponseDto> getAllInvoices(Jwt principal) {
        return invoiceQueryService.getAllInvoicesByUserId(GetAllInvoicesByUserIdQuery.builder()
                .userId(principal.getSubject())
                .build());
    }

    @Override
    public InvoiceResponseDto getInvoiceById(String invoiceId, Jwt principal) {
        return invoiceQueryService.getInvoiceByIdAndUserId(GetInvoiceByIdAndUserIdQuery.builder()
                .invoiceId(invoiceId)
                .userId(principal.getSubject())
                .build());
    }

    @Override
    public ImageUploadAttemptDto startProcessingInvoice(String invoiceId, StartImageUploadInvoiceCommand command, Jwt principal) {
        return invoiceProcessingService.startImageUploadInvoice(command.toBuilder()
                .invoiceId(invoiceId)
                .userId(principal.getSubject())
                .build());
    }
}
