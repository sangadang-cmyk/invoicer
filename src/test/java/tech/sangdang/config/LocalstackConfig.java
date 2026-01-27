package tech.sangdang.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@TestConfiguration
public class LocalstackConfig {
    /**
     * This sets up the local stack container
     */
    @Bean
    LocalStackContainer localStackContainer() {
        return new LocalStackContainer(DockerImageName.parse("localstack/localstack-pro:latest"))
                .withEnv("LOCALSTACK_AUTH_TOKEN", "ls-BijimEYo-POzO-pIYI-7757-fAja6587ede1")
                .withReuse(true)
                        .withCopyFileToContainer(
                                MountableFile.forClasspathResource("aws-init.sh", 777),
                                "/etc/localstack/init/ready.d/aws-init.sh"
                        )
                .waitingFor(Wait.forLogMessage(".*Ready\\.\n", 1));
    }

    /**
     * This runs after the localstack container is started and configures properties in application.yml
     * Why here and not hard-code? Because the endpoint URL is dynamic and may change
     * Note 1: We only need to configure the endpoint. The others can be static as per application.yml (region, access key, secret key - localstack doesn't care about these)
     * Note 2: This only configures spring cloud aws
     */
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry, LocalStackContainer localStackContainer) {
        registry.add("spring.cloud.aws.endpoint", localStackContainer::getEndpoint);
        registry.add("spring.cloud.aws.dynamodb.endpoint", localStackContainer::getEndpoint);
    }
}
