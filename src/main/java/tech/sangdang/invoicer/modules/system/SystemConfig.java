package tech.sangdang.invoicer.modules.system;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "system.config")
public class SystemConfig {
    private String deploymentVersion = "LOCAL";
}
