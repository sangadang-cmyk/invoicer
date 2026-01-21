package tech.sangdang.invoicer.modules.invoice.app.service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.CreateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.DeleteInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.UpdateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;

@Validated
public interface InvoiceManagementService {
    InvoiceResponseDto createInvoice(@Valid CreateInvoiceCommand command);

    InvoiceResponseDto updateInvoice(@Valid UpdateInvoiceCommand command);

    void deleteInvoice(@Valid DeleteInvoiceCommand command);
}
