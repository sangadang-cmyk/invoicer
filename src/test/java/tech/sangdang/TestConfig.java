package tech.sangdang;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@TestConfiguration
public class TestConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer() {
        return new LocalStackContainer(DockerImageName.parse("localstack/localstack:4.3.0"))
                .withServices(LocalStackContainer.Service.DYNAMODB)
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("init-dynamodb-table.sh"),
                        "/etc/localstack/init/ready.d/init-dynamodb-table.sh"
                )
                .waitingFor(Wait.forHealthcheck());
    }

    @DynamicPropertySource
    void dynamicProperties(DynamicPropertyRegistry registry, LocalStackContainer localStackContainer) {
        registry.add("aws.dynamodb.endpoint", () ->
                localStackContainer.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString()
        );
        registry.add("aws.dynamodb.region", localStackContainer::getRegion);
        registry.add("aws.accessKeyId", localStackContainer::getAccessKey);
        registry.add("aws.secretAccessKey", localStackContainer::getSecretKey);
    }

    @Bean
    public DynamoDbClient dynamoDbClient(LocalStackContainer localStack) {
        return DynamoDbClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                .region(Region.of(localStack.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())
                ))
                .build();
    }


    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTemplate dynamoDbTemplate(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        return new DynamoDbTemplate(dynamoDbEnhancedClient);
    }
}
