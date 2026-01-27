package tech.sangdang.cucumber.steps;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import tech.sangdang.cucumber.CucumberStepParent;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class AWSStepDef extends CucumberStepParent {
    @Autowired
    DynamoDbClient dynamoDbClient;
    
    @When("I access DynamoDB")
    public void iAccessDynamoDB() {
        String region = dynamoDbClient.serviceClientConfiguration().region().toString();
        System.out.println("DynamoDB Region: " + region);
        
        assertEquals("DynamoDB region should be ap-southeast-1", "ap-southeast-1", region);
        
        var result = dynamoDbClient.scan(ScanRequest.builder().tableName("invoice").build());
        System.out.println("DynamoDB Scan Result: " + result);
    }

    @Then("I should be able to read and write items")
    public void iShouldBeAbleToReadAndWriteItems() {
        
    }
}
