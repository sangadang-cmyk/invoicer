package tech.sangdang.invoicer.modules.invoice.app.dto.req;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class GetInvoiceByIdQuery {
    @NotBlank
    private String invoiceId;

    @Nullable
    private String userId;
}
