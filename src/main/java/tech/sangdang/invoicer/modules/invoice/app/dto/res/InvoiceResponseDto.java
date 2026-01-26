package tech.sangdang.invoicer.modules.invoice.app.dto.res;

import lombok.Data;
import lombok.NoArgsConstructor;
import tech.sangdang.invoicer.modules.invoice.domain.InvoiceStatus;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class InvoiceResponseDto {
    private String invoiceId;
    private String userId;
    private InvoiceStatus status;
    private String description;
    private Integer maxSizeInBytes;
    private Set<String> allowedTypes;
    private List<String> errorLogs;
    private String createdAt;
    private String updatedAt;
    private String createdByUserId;
}
