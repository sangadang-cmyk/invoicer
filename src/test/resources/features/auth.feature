Feature: Authentication
  Rule: User Authentication
    Scenario Outline: Not-logged-in user tries to access protected resource
      Given I am not logged in
      When I access a "<role>"-protected resource
      Then I shouldnt be granted access
      Examples:
        | role  |
        | ADMIN |
        | USER  |

    Scenario Outline: Authenticate with valid access token
      Given I am logged in as "<role>"
      When I access a "<role>"-protected resource
      Then I should be granted access

      Examples:
        | role  |
        | ADMIN |
        | USER  |

  Rule: Machine-to-machine authentication
    