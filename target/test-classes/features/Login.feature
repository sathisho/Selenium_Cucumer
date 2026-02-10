Feature: User Login Functionality
  As a user of the SauceDemo application
  I want to login with my credentials
  So that I can access the product catalog

  Background:
    Given User navigates to the SauceDemo login page

  @smoke @regression
  Scenario: Successful login with valid credentials
    When User enters username "standard_user"
    And User enters password "secret_sauce"
    And User clicks on login button
    Then User should be redirected to products page
    And User should see "Products" title

  @regression
  Scenario: Failed login with invalid username
    When User enters username "invalid_user"
    And User enters password "secret_sauce"
    And User clicks on login button
    Then User should see error message containing "Username and password do not match"
    And User should remain on login page

  @regression
  Scenario: Failed login with invalid password
    When User enters username "standard_user"
    And User enters password "wrong_password"
    And User clicks on login button
    Then User should see error message containing "Username and password do not match"
    And User should remain on login page

  @regression
  Scenario: Login with empty credentials
    When User clicks on login button
    Then User should see error message containing "Username is required"

  @regression
  Scenario: Login with locked user
    When User enters username "locked_out_user"
    And User enters password "secret_sauce"
    And User clicks on login button
    Then User should see error message containing "this user has been locked out"

  @smoke @regression @dataDriven
  Scenario Outline: Login with multiple user credentials
    When User enters username "<username>"
    And User enters password "<password>"
    And User clicks on login button
    Then User should see "<result>"

    Examples:
      | username                | password     | result                          |
      | standard_user           | secret_sauce | Products                        |
      | locked_out_user         | secret_sauce | locked out                      |
      | problem_user            | secret_sauce | Products                        |
      | performance_glitch_user | secret_sauce | Products                        |
      | invalid_user            | secret_sauce | do not match                    |
      | standard_user           | wrong_pass   | do not match                    |
