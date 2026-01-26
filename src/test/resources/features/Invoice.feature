Feature: Invoice
  Scenario Outline: Create invoice
    Given I have logged in as internal
    When I submit a new invoice with "<description>", "<userId>", <maxSizeInBytes>, and "<allowedTypes>"
    Then I should see the invoice created
    Examples:
      | description    | userId | maxSizeInBytes | allowedTypes    |
      | Test Invoice 1 | 101    | 1048576        | APPLICATION_PDF |
      | Test Invoice 2 | 102    | 2097152        | IMAGE_JPEG      |
      | Test Invoice 3 | 103    | 5242880        | APPLICATION_PDF |

  Scenario Outline: Update invoice
    Given I have logged in as internal
    Given I submit a new invoice with "<description>", "<userId>", <maxSizeInBytes>, and "<allowedTypes>"
    When I update the invoice with new "<new_description>" and <new_maxSizeInBytes>
    Then I should see the invoice updated with new "<new_description>" and <new_maxSizeInBytes>
    Examples:
      | description    | userId | maxSizeInBytes | allowedTypes    | new_description   | new_maxSizeInBytes |
      | Test Invoice 1 | 101    | 1048576        | APPLICATION_PDF | Updated Invoice 1 | 2097152            |
      | Test Invoice 2 | 102    | 2097152        | IMAGE_JPEG      | Updated Invoice 2 | 4194304            |
      | Test Invoice 3 | 103    | 5242880        | APPLICATION_PDF | Updated Invoice 3 | 10485760           |

  Scenario Outline: Delete invoice
    Given I have logged in as internal
    Given I submit a new invoice with "<description>", "<userId>", <maxSizeInBytes>, and "<allowedTypes>"
    When I delete the invoice
    Then I should not see the invoice in the system
    Examples:
      | description    | userId | maxSizeInBytes | allowedTypes    |
      | Test Invoice 1 | 101    | 1048576        | APPLICATION_PDF |
      | Test Invoice 2 | 102    | 2097152        | IMAGE_JPEG      |
      | Test Invoice 3 | 103    | 5242880        | APPLICATION_PDF |