Feature: User

  Background:
    * url baseUrl
    * def userBase = '/users'


  Scenario: List all users

    Given path userBase
    When method GET
    Then status 200