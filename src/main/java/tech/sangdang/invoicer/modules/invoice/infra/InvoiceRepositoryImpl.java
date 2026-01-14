package tech.sangdang.invoicer.modules.invoice.infra;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import tech.sangdang.invoicer.modules.invoice.domain.Invoice;
import tech.sangdang.invoicer.modules.invoice.domain.repository.InvoiceRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final DynamoDbTemplate db;

    @Override
    public Invoice persist(@NonNull Invoice invoice) {
        return db.save(invoice);
    }

    @Override
    public Optional<Invoice> findById(String id) {
        Key key = Key.builder()
                .partitionValue(id)
                .build();
        return Optional.ofNullable(db.load(key, Invoice.class));
    }

    @Override
    public List<Invoice> findAll() {
        return db.scanAll(Invoice.class).items().stream().toList();
    }

    @Override
    public Invoice update(@NonNull Invoice invoice) {
        return db.update(invoice);
    }

    @Override
    public void deleteById(String id) {
        Key key = Key.builder()
                .partitionValue(id)
                .build();
        db.delete(key, Invoice.class);
    }
}
