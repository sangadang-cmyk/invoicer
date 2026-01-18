package tech.sangdang.invoicer.modules.invoice.app.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class GetInvoiceByIdAndUserIdQuery {
    @NotBlank
    private String invoiceId;
    @NotBlank
    private String userId;
}