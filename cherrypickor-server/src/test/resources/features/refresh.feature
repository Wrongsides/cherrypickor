Feature: Item cache refresh

  Background:
    Given Cherrypickor is running
    And ESI services are available

  Scenario: The item refresh is requested
    When I post to the refresh endpoint
    Then the asteroid types are requested from ESI