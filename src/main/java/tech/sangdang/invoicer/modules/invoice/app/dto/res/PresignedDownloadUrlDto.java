package tech.sangdang.invoicer.modules.invoice.app.dto.res;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class PresignedDownloadUrlDto {
    private String downloadUrl;
}
