package tech.sangdang.invoicer.modules.invoice.app.dto.res;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ImageUploadAttemptDto {
    private String uploadUrl;
}
