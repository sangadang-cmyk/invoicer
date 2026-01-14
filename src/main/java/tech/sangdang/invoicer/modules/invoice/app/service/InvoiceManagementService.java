package tech.sangdang.invoicer.modules.invoice.app.service;

import tech.sangdang.invoicer.modules.invoice.app.dto.req.CreateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.DeleteInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.UpdateInvoiceCommand;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;

public interface InvoiceManagementService {
    InvoiceResponseDto createInvoice(CreateInvoiceCommand command);

    InvoiceResponseDto updateInvoice(UpdateInvoiceCommand command);

    void deleteInvoice(DeleteInvoiceCommand command);
}
