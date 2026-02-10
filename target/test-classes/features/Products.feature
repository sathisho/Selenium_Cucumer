Feature: Product Shopping Functionality
  As a logged-in user
  I want to browse and add products to cart
  So that I can purchase items

  Background:
    Given User is logged in to the application

  @smoke @regression
  Scenario: View all products on products page
    Then User should see products page
    And User should see at least 6 products displayed

  @regression
  Scenario: Add single product to cart
    When User adds product "Sauce Labs Backpack" to cart
    Then Shopping cart badge should show "1"

  @regression
  Scenario: Add multiple products to cart
    When User adds product "Sauce Labs Backpack" to cart
    And User adds product "Sauce Labs Bike Light" to cart
    And User adds product "Sauce Labs Bolt T-Shirt" to cart
    Then Shopping cart badge should show "3"

  @regression
  Scenario: Remove product from cart
    Given User adds product "Sauce Labs Backpack" to cart
    When User removes product "Sauce Labs Backpack" from cart
    Then Shopping cart badge should not be displayed

  @regression
  Scenario: Verify product price
    When User checks price of product "Sauce Labs Backpack"
    Then Product price should be "$29.99"

  @smoke @regression
  Scenario: Sort products by price low to high
    When User sorts products by "Price (low to high)"
    Then Products should be sorted correctly

  @regression
  Scenario: User logout from products page
    When User clicks on menu button
    And User clicks on logout link
    Then User should be redirected to login page

  @e2e @regression
  Scenario: Complete shopping flow - Add to cart and view cart
    When User adds product "Sauce Labs Backpack" to cart
    And User adds product "Sauce Labs Bike Light" to cart
    And User clicks on shopping cart
    Then User should see cart page with 2 items
