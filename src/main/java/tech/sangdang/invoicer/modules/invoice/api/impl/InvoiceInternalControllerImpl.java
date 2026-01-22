package tech.sangdang.invoicer.modules.invoice.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sangdang.invoicer.modules.invoice.api.InvoiceInternalController;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.*;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;
import tech.sangdang.invoicer.modules.invoice.app.service.InvoiceManagementService;
import tech.sangdang.invoicer.modules.invoice.app.service.InvoiceQueryService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(InvoiceInternalController.PATH)
public class InvoiceInternalControllerImpl implements InvoiceInternalController {
    private final InvoiceManagementService invoiceManagementService;
    private final InvoiceQueryService invoiceQueryService;

    @Override
    public ResponseEntity<InvoiceResponseDto> createInvoice(CreateInvoiceCommand command, Jwt principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(invoiceManagementService.createInvoice(
                        command.toBuilder().createdByUserId(principal.getSubject()).build()
                ));
    }

    @Override
    public InvoiceResponseDto updateInvoice(String invoiceId, UpdateInvoiceCommand command) {
        return invoiceManagementService.updateInvoice(command.toBuilder()
                .invoiceId(invoiceId)
                .build());
    }

    @Override
    public ResponseEntity<Void> deleteInvoice(String invoiceId) {
        invoiceManagementService.deleteInvoice(DeleteInvoiceCommand.builder()
                .invoiceId(invoiceId)
                .build());
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<InvoiceResponseDto> getAllInvoices() {
        return invoiceQueryService.getAllInvoices(GetAllInvoicesQuery.builder().build());
    }

    @Override
    public InvoiceResponseDto getInvoiceById(String invoiceId) {
        return invoiceQueryService.getInvoiceById(GetInvoiceByIdQuery.builder()
                .invoiceId(invoiceId)
                .build());
    }
}
