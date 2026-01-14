package tech.sangdang.invoicer.modules.invoice.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

    @Override
    public InvoiceResponseDto createInvoice(CreateInvoiceCommand command) {
        Invoice invoice = Invoice.ofNew(
                command.getUserId(),
                new HashSet<>(command.getAllowedTypes()),
                command.getDescription(),
                command.getMaxSizeInBytes()
        );
        return this.invoiceMapper.toResponse(this.invoiceRepository.persist(invoice));
    }

    @Override
    public InvoiceResponseDto updateInvoice(UpdateInvoiceCommand command) {
        Invoice invoice = invoiceRepository.findById(command.getInvoiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found with id: " + command.getInvoiceId()));

        this.invoiceMapper.mapNonNull(command, invoice);

        return this.invoiceMapper.toResponse(this.invoiceRepository.update(invoice));
    }

    @Override
    public void deleteInvoice(DeleteInvoiceCommand command) {
        Invoice invoice = invoiceRepository.findById(command.getInvoiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found with id: " + command.getInvoiceId()));

        if(!invoice.canBeDeleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invoice with id: " + command.getInvoiceId() + " cannot be deleted.");
        }

        this.invoiceRepository.deleteById(command.getInvoiceId());
    }
}
