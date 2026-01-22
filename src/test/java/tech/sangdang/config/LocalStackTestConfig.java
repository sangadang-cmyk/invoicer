package tech.sangdang.config;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@TestConfiguration
public class LocalStackTestConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer() {
        //noinspection resource
        return new LocalStackContainer(DockerImageName.parse("localstack/localstack:4.3.0"))
                .withServices(LocalStackContainer.Service.DYNAMODB)
                .waitingFor(Wait.forHealthcheck());
    }

    @SuppressWarnings("unused")
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
        var client = DynamoDbClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                .region(Region.of(localStack.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())
                ))
                .build();

        client.createTable(CreateTableRequest.builder()
                .tableName("invoice")
                .keySchema(KeySchemaElement.builder()
                        .attributeName("invoiceId")
                        .keyType(KeyType.HASH)
                        .build())
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("invoiceId")
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build());

        return client;
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
