package tech.sangdang.invoicer.modules.invoice.app.service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetAllInvoicesByUserIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetAllInvoicesQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetInvoiceByIdAndUserIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetInvoiceByIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;

import java.util.List;

@Validated
public interface InvoiceQueryService {
    List<InvoiceResponseDto> getAllInvoices(@Valid GetAllInvoicesQuery query);
    InvoiceResponseDto getInvoiceById(@Valid GetInvoiceByIdQuery query);
    List<InvoiceResponseDto> getAllInvoicesByUserId(@Valid GetAllInvoicesByUserIdQuery query);
    InvoiceResponseDto getInvoiceByIdAndUserId(@Valid GetInvoiceByIdAndUserIdQuery query);
}
