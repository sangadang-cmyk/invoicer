Feature: Authentication
  Scenario Outline: <authentication> user (role = <role>) access <resourceRole> resource with result <responseType>
    Given I am an "<authentication>" user with role "<role>"
    When I attempt to access a "<resourceRole>" resource
    Then I should receive a "<responseType>" response

    Examples:
      | authentication  | role  | resourceRole | responseType |
      | unauthenticated |       | PRIVATE      | unauthorized |
      | unauthenticated |       | PUBLIC       | success      |
      | authenticated   | USER  | ADMIN        | forbidden    |
      | authenticated   | ADMIN | USER         | forbidden    |
      | authenticated   | USER  | USER         | success      |
      | authenticated   | ADMIN | ADMIN        | success      |