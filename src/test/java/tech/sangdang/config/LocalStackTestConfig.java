package tech.sangdang.config;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;

/**
 * This file starts a localstack instance with the configured services needed for this application
 */
@TestConfiguration
public class LocalStackTestConfig {
//    @Bean(initMethod = "start")
//    @ServiceConnection
//    public LocalStackContainer localStackContainer() {
//        //noinspection resource
//        return new LocalStackContainer(DockerImageName.parse("localstack/localstack-pro:latest"))
////                .withServices(
////                        LocalStackContainer.Service.DYNAMODB
//////                        LocalStackContainer.Service.
////                )
//                .withEnv("LOCALSTACK_AUTH_TOKEN", "ls-BijimEYo-POzO-pIYI-7757-fAja6587ede1")
//                .withReuse(true)
//                .withCopyFileToContainer(
//                        MountableFile.forClasspathResource("aws-init.sh", 777),
//                        "/etc/localstack/init/ready.d/aws-init.sh"
//                )
//                .waitingFor(Wait.forLogMessage(".*Ready\\.\n", 1));
//    }
//
//    @SuppressWarnings("unused")
//    @DynamicPropertySource
//    void dynamicProperties(DynamicPropertyRegistry registry, LocalStackContainer localStackContainer) {
////        registry.add("aws.dynamodb.endpoint", () ->
////                localStackContainer.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString()
////        );
////        registry.add("spring.cloud.aws.region.static", localStackContainer::getRegion);
//        registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
//        registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);
//    }
//
////    @Bean
////    public DynamoDbClient dynamoDbClient(LocalStackContainer localStack) {
////        return DynamoDbClient.builder()
////                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
////                .region(Region.of(localStack.getRegion()))
////                .credentialsProvider(StaticCredentialsProvider.create(
////                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())
////                ))
////                .build();
////    }
//
////
////    @Bean
////    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
////        return DynamoDbEnhancedClient.builder()
////                .dynamoDbClient(dynamoDbClient)
////                .build();
////    }
////
////    @Bean
////    public DynamoDbTemplate dynamoDbTemplate(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
////        return new DynamoDbTemplate(dynamoDbEnhancedClient);
////    }
}
