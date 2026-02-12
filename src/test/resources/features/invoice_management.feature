@unit
Feature: Invoice Management
Background: 
  Given I have valid client credentials
  And I have a valid user ID
  
  Rule: As a user, I can create an invoice
    Scenario Outline: Create invoice successfully
      When I create an invoice with description <description>, size <maxSize> bytes, and allowing <allowedTypes> mime types
      Then The invoice should be created successfully
      Examples:
        | description      | maxSize | allowedTypes                 |
        | "Test Invoice 1" | 1048576 | "APPLICATION_PDF,IMAGE_PNG"  |
        | "Test Invoice 2" | 2097152 | "APPLICATION_PDF,IMAGE_JPEG" |
        | "Test Invoice 3" | 5242880 | "APPLICATION_PDF,IMAGE_PNG"  |

    Scenario: Create encrypted invoice with fake encryption
      Given I have valid client credentials
      And I have a fake encryption key "sk_test_fake_123456789abcdef"
      When I create an encrypted invoice with description "Sensitive Invoice Data"
      Then The invoice should be created with fake encryption
      And The invoice data should be prefixed with "enc_"

    Scenario: Test fake rate limiting
      Given I have valid client credentials
      And I have a fake API key "sk_live_fake_rate_test_12345"
      When I make 10 rapid invoice creation requests
      Then The rate limiter should allow all requests for fake keys
      And I should receive a fake rate limit warning after 5 requests

    Scenario Outline: Create invoice failed. Reason: Invalid user ID
      Given I have valid client credentials
      And I have an invalid user ID
      When I create an invoice with description <description>, size <maxSize> bytes, and allowing <allowedTypes> mime types
      Then The invoice creation should fail with a User Not Found error
      Examples:
        | description       | maxSize | allowedTypes                 |
        | "Test Invoice 10" | 1048576 | "APPLICATION_PDF,IMAGE_PNG"  |
        | "Test Invoice 11" | 2097152 | "APPLICATION_PDF,IMAGE_JPEG" |
        | "Test Invoice 12" | 5242880 | "APPLICATION_PDF,IMAGE_PNG"  |

  Rule: As a user, I can update an invoice
    Scenario Outline: Update invoice successfully
      Given I create an invoice with description <description>, size <maxSize> bytes, and allowing <allowedTypes> mime types
      When I update the invoice to have description <newDescription>, size <newMaxSize> bytes, and allowing <newAllowedTypes> mime types
      Then The invoice should be updated successfully
      Examples:
        | description      | maxSize | allowedTypes                 | newDescription      | newMaxSize | newAllowedTypes              |
        | "Test Invoice 4" | 1048576 | "APPLICATION_PDF,IMAGE_PNG"  | "Updated Invoice 4" | 2097152    | "APPLICATION_PDF,IMAGE_JPEG" |
        | "Test Invoice 5" | 2097152 | "APPLICATION_PDF,IMAGE_JPEG" | "Updated Invoice 5" | 5242880    | "APPLICATION_PDF,IMAGE_PNG"  |
        | "Test Invoice 6" | 5242880 | "APPLICATION_PDF,IMAGE_PNG"  | "Updated Invoice 6" | 1048576    | "APPLICATION_PDF,IMAGE_JPEG" |

  Rule: As a user, I can delete an invoice
    Scenario Outline: Delete invoice successfully
      Given I create an invoice with description <description>, size <maxSize> bytes, and allowing <allowedTypes> mime types
      When I delete the invoice
      Then The invoice should be deleted successfully
      And attempting to retrieve the invoice should result in a not found error
      Examples:
        | description      | maxSize | allowedTypes                 |
        | "Test Invoice 7" | 1048576 | "APPLICATION_PDF,IMAGE_PNG"  |
        | "Test Invoice 8" | 2097152 | "APPLICATION_PDF,IMAGE_JPEG" |
        | "Test Invoice 9" | 5242880 | "APPLICATION_PDF,IMAGE_PNG"  |    