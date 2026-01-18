package tech.sangdang.invoicer.modules.invoice.app.dto.req;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class GetAllInvoicesQuery {
    @Nullable
    private String userId;
}
