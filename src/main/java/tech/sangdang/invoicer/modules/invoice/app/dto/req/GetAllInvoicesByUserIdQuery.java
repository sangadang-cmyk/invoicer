package tech.sangdang.invoicer.modules.invoice.app.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class GetAllInvoicesByUserIdQuery {
    @NotBlank
    private String userId;
}
