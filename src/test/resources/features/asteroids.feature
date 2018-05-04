Feature: Asteroid appraisal

  Background:
    Given Cherrypickor is running
    And ESI services are available

  Scenario: The asteroids are sorted by value descending
    When I post a list of asteroids
    Then the result should be ordered highest value first