package tech.sangdang.invoicer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.sangdang.invoicer.common.constants.AppSecurity;
import tech.sangdang.invoicer.modules.system.SystemConfig;

@RequiredArgsConstructor
@Configuration
public class OpenAPIConfig {
    private final SystemConfig systemConfig;
//    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String domainUrl = "https://auth.sangdang.tech";

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
                        .addSecuritySchemes(AppSecurity.OAUTH2, new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(this.domainUrl + "/oauth2/authorize")
                                                .tokenUrl(this.domainUrl + "/oauth2/token")
                                                .scopes(new Scopes())
                                        )
                                )
                        )
                        .addSecuritySchemes("Bearer", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .name("Bearer")
                        )
                )
                ;
    }
}
