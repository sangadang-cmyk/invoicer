package tech.sangdang.invoicer.modules.invoice.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tech.sangdang.invoicer.common.core.FindQuery;
import tech.sangdang.invoicer.common.core.SearchCriteria;
import tech.sangdang.invoicer.common.core.SearchOperation;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetAllInvoicesByUserIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetAllInvoicesQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetInvoiceByIdAndUserIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.req.GetInvoiceByIdQuery;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;
import tech.sangdang.invoicer.modules.invoice.app.mapper.InvoiceMapper;
import tech.sangdang.invoicer.modules.invoice.app.service.InvoiceQueryService;
import tech.sangdang.invoicer.modules.invoice.domain.repository.InvoiceRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvoiceQueryServiceImpl implements InvoiceQueryService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public List<InvoiceResponseDto> getAllInvoices(GetAllInvoicesQuery query) {
        return invoiceRepository
                .find(FindQuery.empty())
                .stream().map(invoiceMapper::toResponse).toList();
    }

    @Override
    public InvoiceResponseDto getInvoiceById(GetInvoiceByIdQuery query) {
        return invoiceRepository.findById(query.getInvoiceId())
                .map(invoiceMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found with id: " + query.getInvoiceId()));
    }

    @Override
    public List<InvoiceResponseDto> getAllInvoicesByUserId(GetAllInvoicesByUserIdQuery query) {
        return invoiceRepository.find(
                        FindQuery.builder()
                                .searchCriteria(
                                        List.of(SearchCriteria.builder()
                                                .key("userId")
                                                .value(query.getUserId())
                                                .operation(SearchOperation.EQUALS)
                                                .build())
                                )
                                .build()
                )
                .stream().map(invoiceMapper::toResponse).toList();
    }

    @Override
    public InvoiceResponseDto getInvoiceByIdAndUserId(GetInvoiceByIdAndUserIdQuery query) {
        var invoice = invoiceRepository.findById(query.getInvoiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found with id: " + query.getInvoiceId()));
        
        if (!invoice.getUserId().equals(query.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to invoice with id: " + query.getInvoiceId());
        }
        
        return this.invoiceMapper.toResponse(invoice);
    }
}
