Feature: Can access AWS
  Scenario: Dynamodb
    When I access DynamoDB
    Then I should be able to read and write items