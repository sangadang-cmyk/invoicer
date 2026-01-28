package tech.sangdang.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.StringUtils;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClientBuilder;
import tech.sangdang.config.LocalstackConfig;
import tech.sangdang.invoicer.InvoicerApplication;
import tech.sangdang.invoicer.common.constants.AppScopes;
import tech.sangdang.invoicer.common.constants.AppSecurity;
import tools.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ContextConfiguration(classes = {ScenarioContext.class, LocalstackConfig.class})
@SpringBootTest(
        classes = {InvoicerApplication.class}, 
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CucumberStepParent {

    public static final LocalStackContainer localStackContainer =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack-pro:latest"))
                    .withServices("s3", "dynamodb", "cognito-idp", "lambda")
                    .withEnv("LOCALSTACK_AUTH_TOKEN", System.getenv("LOCALSTACK_AUTH_TOKEN"))
                    .withReuse(true)
                    // copy the entire localstack folder (which contains init scripts) into the container
                    .withCopyFileToContainer(
                            MountableFile.forHostPath("localstack", 777),
                            "/etc/localstack/init/ready.d/"
                    )
                    .waitingFor(Wait.forLogMessage(".*Ready\\.\n", 1));

    static {
        localStackContainer.start();
    }

    @Autowired
    private AppScopes scopes;

    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * This runs after the localstack container is started and configures properties in application.yml
     * Why here and not hard-code? Because the endpoint URL is dynamic and may change
     * Note 1: We only need to configure the endpoint. The others can be static as per application.yml (region, access key, secret key - localstack doesn't care about these)
     * Note 2: This only configures spring cloud aws. It doesn't configure the cognito client. 
     */
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        log.info("Configuring dynamic properties for Localstack at endpoint: {}", localStackContainer.getEndpoint());
        registry.add("spring.cloud.aws.endpoint", localStackContainer::getEndpoint);
        registry.add("spring.cloud.aws.dynamodb.endpoint", localStackContainer::getEndpoint);
        registry.add("spring.cloud.aws.s3.endpoint", localStackContainer::getEndpoint);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> localStackContainer.getEndpoint() + "/ap-southeast-1_localpoolid");
        registry.add("system.config.auth-url", () -> localStackContainer.getEndpoint() + "/_aws/cognito-idp");
        registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);
    }
    
    public SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getRoleJwt(
            String userId,
            String... roles
    ) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles.length == 0) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + AppSecurity.Role.USER));
        } else {
            Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }

        return SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(builder -> builder
                        .subject(userId)
                )
                .authorities(authorities);
    }
    
    public SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getScopeJwt(
            String sub,
            String... selectedScopes
    ) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (selectedScopes.length == 0) {
            authorities.add(new SimpleGrantedAuthority(scopes.DEFAULT));
        } else {
            Arrays.stream(selectedScopes).forEach(scope -> authorities.add(new SimpleGrantedAuthority(scope)));
        }

        return SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(builder -> builder
                        .subject(sub)
                )
                .authorities(authorities);
    }
    
    public static MockHttpServletRequestBuilder ensureAuth(
            MockHttpServletRequestBuilder requestBuilder,
            SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor auth
    ) {
        if (auth != null) {
            return requestBuilder.with(auth);
        } else {
            return requestBuilder;
        }
    }
    
    public <T> T parseBodyFromResponse(MvcResult response, Class<T> clazz) throws UnsupportedEncodingException {
        var rawResponseBody = response.getResponse().getContentAsString();
        return objectMapper.readValue(rawResponseBody, clazz);
    }
}
