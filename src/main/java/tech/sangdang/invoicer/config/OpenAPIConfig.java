package tech.sangdang.invoicer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.sangdang.invoicer.modules.system.SystemConfig;

@RequiredArgsConstructor
@Configuration
public class OpenAPIConfig {
    private final SystemConfig systemConfig;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Invoicer API")
                        .description("An app that sends invoices to people. Currently, only supports file-based invoices - meaning you can request a file from a user.")
                        .version(systemConfig.getDeploymentVersion())
                        .contact(new Contact()
                                .name("Sang Dang")
                                .email("danganhsang09@gmail.com")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .components(new Components()
                        .addSecuritySchemes("BasicAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                        )
                )
                ;
    }
}
