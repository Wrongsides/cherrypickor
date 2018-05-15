Feature: Asteroid appraisal

  Background:
    Given Cherrypickor is running
    And ESI services are available

  Scenario: The asteroids are sorted by value descending
    When I post 'AsteroidsRequestBody.json' to the asteroids endpoint
    Then the result should be ordered highest value first

  Scenario: The scanner output is sorted by value descending
    When I post 'SurveyScannerRequestBody' to the asteroids endpoint
    Then the result should be ordered highest value first