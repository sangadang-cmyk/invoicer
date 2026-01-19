package tech.sangdang.invoicer.modules.invoice.domain.repository;

import lombok.NonNull;
import tech.sangdang.invoicer.common.core.QueryDynamoRepository;
import tech.sangdang.invoicer.modules.invoice.domain.Invoice;

import java.util.Optional;

public interface InvoiceRepository extends QueryDynamoRepository<Invoice> {
    Invoice persist(@NonNull Invoice invoice);

    Optional<Invoice> findById(String id);
    
    Invoice update(@NonNull Invoice invoice);

    void deleteById(String id);
}
