package tech.sangdang.invoicer.modules.invoice.app.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.sangdang.invoicer.modules.invoice.domain.InvoiceAllowedTypes;

import java.util.Set;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UpdateInvoiceCommand {
    @JsonIgnore
    @NotBlank
    private String invoiceId;
    
    private String description;
    private Integer maxSizeInBytes;
    private Set<InvoiceAllowedTypes> allowedTypes;
}
