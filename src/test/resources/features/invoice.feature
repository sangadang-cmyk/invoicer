Feature: Invoice Management

  Scenario Outline: Create invoice successfully
    Given I have valid client credentials
    And I have a valid user ID
    When I create an invoice with description "<description>", size "<maxSize>" bytes, and allowing "<allowedTypes>" mime types
    Then The invoice should be created successfully
    Examples:
      | description      | maxSize | allowedTypes                 |
      | "Test Invoice 1" | 1048576 | "application/pdf,image/png"  |
      | "Test Invoice 2" | 2097152 | "application/pdf,image/jpeg" |
      | "Test Invoice 3" | 5242880 | "application/pdf,image/gif"  |