package tech.sangdang.invoicer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClientBuilder;

import java.net.URI;

@Slf4j
@Configuration
public class CognitoConfig {
    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.endpoint:}")
    private String endpoint;

    @Value("${spring.cloud.aws.credentials.access-key:}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key:}")
    private String secretKey;

    @Bean
    @Profile("!test")
    CognitoIdentityProviderClient cognitoIdentityProviderClient(
            AwsCredentialsProvider credentialsProvider,
            AwsRegionProvider regionProvider
    ) {
        log.debug("CognitoClient: Connecting to region: {}, endpoint: {}", regionProvider.getRegion(), endpoint);
        CognitoIdentityProviderClientBuilder builder = CognitoIdentityProviderClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(regionProvider.getRegion());

        // if endpoint value is set in properties, then assign it here. else use default endpoint
        if (StringUtils.hasText(endpoint)) {
            builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }
}
