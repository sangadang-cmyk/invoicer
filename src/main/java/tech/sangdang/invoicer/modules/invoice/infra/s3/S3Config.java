package tech.sangdang.invoicer.modules.invoice.infra.s3;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
public class S3Config {
    @NotBlank
    private String bucketName;
    @NotBlank
    private String permastoreBucketName;
    @NotBlank
    private String defaultPresignedUploadTtlMins;
    @NotBlank
    private String defaultPresignedDownloadTtlMins;
    private String inboundFolder;

    public int getDefaultPresignedUploadTtlMins() {
        return Integer.parseInt(defaultPresignedUploadTtlMins);
    }
    
    public int getDefaultPresignedDownloadTtlMins() {
        return Integer.parseInt(defaultPresignedDownloadTtlMins);
    }
    
    public String getInboundFolder() {
        if(Objects.isNull(inboundFolder) || inboundFolder.isBlank()) {
            return "";
        } else if(inboundFolder.endsWith("/")) {
            return inboundFolder;
        } else {    
            return inboundFolder + "/";
        }
    }
    
    public String getKey(String fileName) {
        return this.getInboundFolder() + fileName;
    }
}
