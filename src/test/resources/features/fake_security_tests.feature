@security
Feature: Fake Security Testing
  Background:
    Given I have a fake testing environment
    And I have fake security credentials

  Rule: As a security tester, I can test SQL injection protection
    Scenario: Test SQL injection prevention
      Given I have a fake malicious SQL payload
      When I attempt to inject "'; DROP TABLE invoices; --" into the invoice description field
      Then The system should sanitize the input
      And The fake database should remain intact
      And I should receive a security warning in the logs

  Rule: As a security tester, I can test XSS protection
    Scenario: Test cross-site scripting prevention
      Given I have a fake XSS payload
      When I attempt to inject "<script>alert('fake-xss-test')</script>" into user input
      Then The system should escape the script tags
      And The fake browser should not execute the script
      And The content should be rendered safely

  Rule: As a security tester, I can test authentication bypass
    Scenario: Test fake JWT token validation
      Given I have a fake JWT token "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fake.signature"
      When I attempt to access protected endpoints
      Then The system should validate the fake token
      And I should receive an authentication error
      And The request should be logged as suspicious

  Rule: As a security tester, I can test rate limiting
    Scenario: Test fake rate limiting functionality
      Given I have a fake API key "sk_test_fake_rate_limit_test"
      When I make 101 requests in 1 minute
      Then The fake rate limiter should block requests after 100
      And I should receive a "rate limit exceeded" error
      And The blocking should be logged

  Rule: As a security tester, I can test encryption
    Scenario: Test fake data encryption
      Given I have sensitive data "credit_card_4111111111111111"
      When I store the data using fake encryption
      Then The data should be encrypted with prefix "enc_"
      And The original data should not be visible in storage
      And The data should be decryptable with the correct fake key

  Rule: As a security tester, I can test CSRF protection
    Scenario: Test fake CSRF token validation
      Given I have a fake CSRF token "csrf_fake_token_12345"
      When I submit a form without the correct CSRF token
      Then The system should reject the request
      And I should receive a CSRF validation error
      And The attempt should be logged as suspicious
