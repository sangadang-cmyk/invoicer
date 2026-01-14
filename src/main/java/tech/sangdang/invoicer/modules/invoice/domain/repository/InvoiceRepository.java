package tech.sangdang.invoicer.modules.invoice.domain.repository;

import lombok.NonNull;
import tech.sangdang.invoicer.modules.invoice.domain.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {
    Invoice persist(@NonNull Invoice invoice);

    Optional<Invoice> findById(String id);

    List<Invoice> findAll();

    Invoice update(@NonNull Invoice invoice);

    void deleteById(String id);
}
