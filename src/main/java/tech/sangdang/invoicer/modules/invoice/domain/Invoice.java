package tech.sangdang.invoicer.modules.invoice.domain;

import lombok.Data;
import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@DynamoDbBean
public class Invoice {
    private String invoiceId;
    private String userId;
    private String status;
    private String description;
    private Integer maxSizeInBytes;
    private Set<String> allowedTypes;
    private List<String> errorLogs;
    private String createdAt;
    private String updatedAt;
    private String createdByUserId;

    @DynamoDbPartitionKey
    public String getInvoiceId() {
        return this.invoiceId;
    }
    
    public void setStatus(InvoiceStatus status) {
        this.status = status.name();
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public static Invoice ofDefault() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(Invoice.generateInvoiceId());
        invoice.setCreatedAt(Instant.now().toString());
        invoice.setUpdatedAt(Instant.now().toString());
        invoice.setErrorLogs(List.of());
        return invoice;
    }

    public static Invoice ofNew(
            @NonNull String userId,
            @NonNull Set<InvoiceAllowedTypes> allowedTypes,
            @NonNull String description,
            @NonNull Integer maxSizeInBytes,
            @NonNull String createdByUserId
    ) {
        Invoice invoice = Invoice.ofDefault();
        invoice.setStatus(InvoiceStatus.AWAITING_UPLOAD);
        invoice.setUserId(userId);
        invoice.setAllowedTypes(allowedTypes
                .stream()
                .map(InvoiceAllowedTypes::getValue)
                .collect(Collectors.toSet()));
        invoice.setDescription(description);
        invoice.setMaxSizeInBytes(maxSizeInBytes);
        invoice.setCreatedByUserId(createdByUserId);
        return invoice;
    }

    public static String generateInvoiceId() {
        return "INV_" + java.util.UUID.randomUUID();
    }

    public boolean canBeDeleted() {
        return this.status.equals(InvoiceStatus.AWAITING_UPLOAD.name());
    }

    public boolean canBeStarted() {
        return this.status.equals(InvoiceStatus.AWAITING_UPLOAD.name());
    }

    public boolean canBeUpdated() {
        return this.status.equals(InvoiceStatus.AWAITING_UPLOAD.name());
    }
}
