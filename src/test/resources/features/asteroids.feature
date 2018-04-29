Feature: Asteroid appraisal

  Scenario: The asteroids are sorted by value descending
    When I post a list of asteroids
    Then the result should be ordered highest value first