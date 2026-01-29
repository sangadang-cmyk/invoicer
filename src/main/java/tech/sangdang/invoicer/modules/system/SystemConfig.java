package tech.sangdang.invoicer.modules.system;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "system.config")
public class SystemConfig {
    private String deploymentVersion = "LOCAL";
    
    @NotBlank
    private String authUrl;
    
    @NotBlank
    private String userPoolId;
    
    @Nullable
    private String localstackAuthToken;
    
    @NotBlank
    private String snsTopicArn;
}
