package tech.sangdang.invoicer.modules.invoice.app.service;

import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetAllInvoicesByCustomerIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetAllInvoicesQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetInvoiceByIdAndCustomerIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetInvoiceByIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;

import java.util.List;

public interface InvoiceQueryService {
    List<InvoiceResponseDto> getAllInvoices(GetAllInvoicesQuery query);
    InvoiceResponseDto getInvoiceById(GetInvoiceByIdQuery query);
    void getAllInvoicesByCustomerId(GetAllInvoicesByCustomerIdQuery query);
    void getInvoiceByIdAndCustomerId(GetInvoiceByIdAndCustomerIdQuery query);
}
