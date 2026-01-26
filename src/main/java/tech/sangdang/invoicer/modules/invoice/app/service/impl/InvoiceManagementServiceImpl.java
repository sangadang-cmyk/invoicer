package tech.sangdang.invoicer.modules.invoice.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sangdang.invoicer.modules.invoice.domain.error.InvoiceCannotBeDeletedError;
import tech.sangdang.invoicer.modules.invoice.domain.error.InvoiceCannotBeUpdatedError;
import tech.sangdang.invoicer.modules.invoice.domain.error.InvoiceNotFoundError;
import tech.sangdang.invoicer.modules.account.domain.error.UserNotFoundError;
import tech.sangdang.invoicer.modules.account.domain.ports.AccountQueryPort;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.CreateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.DeleteInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.UpdateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;
import tech.sangdang.invoicer.modules.invoice.app.mapper.InvoiceMapper;
import tech.sangdang.invoicer.modules.invoice.app.service.InvoiceManagementService;
import tech.sangdang.invoicer.modules.invoice.domain.Invoice;
import tech.sangdang.invoicer.modules.invoice.domain.repository.InvoiceRepository;

import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvoiceManagementServiceImpl implements InvoiceManagementService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final AccountQueryPort accountQueryPort;

    @Override
    public InvoiceResponseDto createInvoice(CreateInvoiceCommand command) {
        // validate user id existence
        var existsUser = accountQueryPort.existsUserById(command.getUserId());
        if(!existsUser) {
            throw new UserNotFoundError("ID", command.getUserId());
        }
        
        Invoice invoice = Invoice.ofNew(
                command.getUserId(),
                new HashSet<>(command.getAllowedTypes()),
                command.getDescription(),
                command.getMaxSizeInBytes(),
                command.getCreatedByUserId()
        );
        var persistedInvoice = this.invoiceRepository.persist(invoice);
        return this.invoiceMapper.toResponse(persistedInvoice);
    }

    @Override
    public InvoiceResponseDto updateInvoice(UpdateInvoiceCommand command) {
        Invoice invoice = invoiceRepository.findById(command.getInvoiceId())
                .orElseThrow(() -> new InvoiceNotFoundError("ID", command.getInvoiceId()));
        
        if(!invoice.canBeUpdated()) {
            throw new InvoiceCannotBeUpdatedError(invoice.getInvoiceId());
        }

        this.invoiceMapper.mapNonNull(command, invoice);

        return this.invoiceMapper.toResponse(this.invoiceRepository.update(invoice));
    }

    @Override
    public void deleteInvoice(DeleteInvoiceCommand command) {
        Invoice invoice = invoiceRepository.findById(command.getInvoiceId())
                .orElseThrow(() -> new InvoiceNotFoundError("ID", command.getInvoiceId()));

        if(!invoice.canBeDeleted()) {
            throw new InvoiceCannotBeDeletedError(invoice.getInvoiceId());
        }

        this.invoiceRepository.deleteById(command.getInvoiceId());
    }
}
