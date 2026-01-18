package tech.sangdang.invoicer.modules.invoice.infra.s3;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
public class S3Config {
    @NotBlank
    private String bucketName;
    @NotBlank
    private String defaultPresignedUploadTtlMins;

    public int getDefaultPresignedUploadTtlMins() {
        return Integer.parseInt(defaultPresignedUploadTtlMins);
    }
}
