package tech.sangdang.invoicer.modules.invoice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import tech.sangdang.invoicer.modules.invoice.app.dto.res.InvoiceResponseDto;

import java.util.List;

@Tag(name = "Invoice")
public interface InvoiceUserController {
    String PATH = "/user/invoice";

    @Operation(summary = "Get my invoices")
    @GetMapping("/batch")
    List<InvoiceResponseDto> getAllInvoices();

    @Operation(summary = "Get my invoice by ID")
    @GetMapping("/{invoiceId}")
    InvoiceResponseDto getInvoiceById(@PathVariable String invoiceId);

    @Operation(summary = "Complete my invoice by ID")
    @PutMapping("/{id}/complete")
    void completeMyInvoiceById();
}
