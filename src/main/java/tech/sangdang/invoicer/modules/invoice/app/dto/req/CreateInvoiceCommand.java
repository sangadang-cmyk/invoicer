package tech.sangdang.invoicer.modules.invoice.app.dto.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.sangdang.invoicer.modules.invoice.domain.InvoiceAllowedTypes;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CreateInvoiceCommand {
    private String userId;
    private List<InvoiceAllowedTypes> allowedTypes;
    private String description;
    private Integer maxSizeInBytes;
}
