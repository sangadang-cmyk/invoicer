package tech.sangdang.invoicer.modules.invoice.app.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.sangdang.invoicer.modules.invoice.domain.InvoiceAllowedTypes;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CreateInvoiceCommand {
    @NotBlank
    private String userId;
    
    private List<InvoiceAllowedTypes> allowedTypes;
    
    @NotBlank
    private String description;
    
    @Positive
    private Integer maxSizeInBytes;
    
    @JsonIgnore
    @NotBlank
    private String createdByUserId;
}
