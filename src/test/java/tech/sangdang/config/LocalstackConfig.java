package tech.sangdang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClientBuilder;

import java.net.URI;

import static tech.sangdang.cucumber.CucumberStepParent.localStackContainer;

@Slf4j
@TestConfiguration
public class LocalstackConfig {
    @Bean
    @Profile("test")
    CognitoIdentityProviderClient cognitoIdentityProviderClient(
            AwsCredentialsProvider credentialsProvider,
            AwsRegionProvider regionProvider
    ) {
        String endpoint = localStackContainer.getEndpoint().toString();

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
