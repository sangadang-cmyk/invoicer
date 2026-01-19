package tech.sangdang.invoicer.modules.invoice.infra;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import tech.sangdang.invoicer.common.core.BaseDynamoRepositoryImpl;
import tech.sangdang.invoicer.modules.invoice.domain.Invoice;
import tech.sangdang.invoicer.modules.invoice.domain.repository.InvoiceRepository;

import java.util.Optional;

@Slf4j
@Repository
public class InvoiceRepositoryImpl extends BaseDynamoRepositoryImpl<Invoice> implements InvoiceRepository {
    public InvoiceRepositoryImpl(DynamoDbTemplate dbTemplate) {
        super(dbTemplate, Invoice.class);
    }

    @Override
    public Invoice persist(@NonNull Invoice invoice) {
        return super.db.save(invoice);
    }

    @Override
    public Optional<Invoice> findById(String id) {
        Key key = Key.builder()
                .partitionValue(id)
                .build();
        return Optional.ofNullable(super.db.load(key, Invoice.class));
    }

    @Override
    public Invoice update(@NonNull Invoice invoice) {
        return super.db.update(invoice);
    }

    @Override
    public void deleteById(String id) {
        Key key = Key.builder()
                .partitionValue(id)
                .build();
        super.db.delete(key, Invoice.class);
    }
}
