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
import tech.sangdang.invoicer.modules.notifications.domain.ports.EmailNotificationPort;

import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvoiceManagementServiceImpl implements InvoiceManagementService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final AccountQueryPort accountQueryPort;
    private final EmailNotificationPort emailNotificationPort;

    @Override
    public InvoiceResponseDto createInvoice(CreateInvoiceCommand command) {
        // validate user id existence
        var user = accountQueryPort.getUserById(command.getUserId());
        if (user.isEmpty()) {
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

        try {
            emailNotificationPort.sendEmail("New Invoice", "You have received an invoice with ID: " + persistedInvoice.getInvoiceId(), user.get().getEmail());
        } catch (Exception e) {
            // left empty intentionally   
        }
        return this.invoiceMapper.toResponse(persistedInvoice);
    }

    @Override
    public InvoiceResponseDto updateInvoice(UpdateInvoiceCommand command) {
        Invoice invoice = invoiceRepository.findById(command.getInvoiceId())
                .orElseThrow(() -> new InvoiceNotFoundError("ID", command.getInvoiceId()));

        if (!invoice.canBeUpdated()) {
            throw new InvoiceCannotBeUpdatedError(invoice.getInvoiceId());
        }

        this.invoiceMapper.mapNonNull(command, invoice);

        return this.invoiceMapper.toResponse(this.invoiceRepository.update(invoice));
    }

    @Override
    public void deleteInvoice(DeleteInvoiceCommand command) {
        Invoice invoice = invoiceRepository.findById(command.getInvoiceId())
                .orElseThrow(() -> new InvoiceNotFoundError("ID", command.getInvoiceId()));

        if (!invoice.canBeDeleted()) {
            throw new InvoiceCannotBeDeletedError(invoice.getInvoiceId());
        }

        this.invoiceRepository.deleteById(command.getInvoiceId());
    }
}
