Feature: Invoice Processing
  Background:
    Given I have valid client credentials
    And I have a valid user ID
    And I have created an invoice with description "Process Invoice", size 1048576 bytes, and allowing "APPLICATION_PDF" mime types
    
  Rule: As a user, I can start processing an invoice
    @integration
    Scenario: Start processing successfully
      Given I am logged in as role USER with the user ID I have
      And I have a sample file "sample_upload.pdf"
      When I start processing the invoice I created before
      Then I should receive an S3 presigned URL
      When I upload the sample file to the presigned URL
      Then The invoice status should be updated to "PENDING_VALIDATION" within 10 seconds
      And There should exist a new item in the processed bucket
      When I request a presigned download URL for the invoice I created before
      Then I should receive a valid presigned download URL
      
    Scenario: Start processing with invalid uploaded file
      Given I am logged in as role USER with the user ID I have
      And I have a sample file "sample_upload.png"
      When I start processing the invoice I created before
      Then I should receive an S3 presigned URL
      When I upload the sample file to the presigned URL
      Then The invoice status should be updated to "REJECTED" within 10 seconds