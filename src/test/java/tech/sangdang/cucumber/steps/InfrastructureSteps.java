package tech.sangdang.cucumber.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import tech.sangdang.cucumber.CucumberStepParent;

@Slf4j
public class InfrastructureSteps extends CucumberStepParent {
    @Autowired
    DynamoDbClient dynamoDbClient;

    @Autowired
    S3Client s3Client;

    @Autowired
    CognitoIdentityProviderClient cognitoClient;

    @When("I run infrastructure tests")
    public void iRunInfrastructureTests() {
        log.info("Running infrastructure tests against Localstack:");
        log.info("DynamoDB Endpoint: {}, region: {}",
                dynamoDbClient.serviceClientConfiguration().endpointOverride(),
                dynamoDbClient.serviceClientConfiguration().region()
        );
        log.info("S3 Endpoint: {}, region: {}",
                s3Client.serviceClientConfiguration().endpointOverride(),
                s3Client.serviceClientConfiguration().region()
        );
        log.info("Cognito Endpoint: {}, region: {}",
                cognitoClient.serviceClientConfiguration().endpointOverride(),
                cognitoClient.serviceClientConfiguration().region()
        );
    }

    @Then("all infrastructure tests should pass")
    public void allInfrastructureTestsShouldPass() {
        // Placeholder for infrastructure test assertions
    }
}
