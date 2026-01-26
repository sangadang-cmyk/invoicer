package tech.sangdang.invoicer.modules.account.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClientBuilder;

import java.net.URI;

@Configuration
public class CognitoConfig {
    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.endpoint:}")
    private String endpoint;

    @Bean
    CognitoIdentityProviderClient cognitoIdentityProviderClient() {
        CognitoIdentityProviderClientBuilder builder = CognitoIdentityProviderClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.builder().build());

        // if endpoint value is set in properties, then assign it here. else use default endpoint
        if (StringUtils.hasText(endpoint)) { 
            builder.endpointOverride(URI.create(endpoint));
        }
        
        return builder.build();
    }
}
