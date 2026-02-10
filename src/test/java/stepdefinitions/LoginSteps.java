package stepdefinitions;

import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ConfigReader;
import utils.DriverManager;

/**
 * Step Definitions for Login feature
 */
public class LoginSteps {
    
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private ConfigReader config;
    
    public LoginSteps() {
        loginPage = new LoginPage(DriverManager.getDriver());
        productsPage = new ProductsPage(DriverManager.getDriver());
        config = new ConfigReader();
    }
    
    @Given("User navigates to the SauceDemo login page")
    public void user_navigates_to_the_sauce_demo_login_page() {
        DriverManager.getDriver().get(config.getBaseUrl());
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed");
    }
    
    @When("User enters username {string}")
    public void user_enters_username(String username) {
        loginPage.enterUsername(username);
    }
    
    @When("User enters password {string}")
    public void user_enters_password(String password) {
        loginPage.enterPassword(password);
    }
    
    @When("User clicks on login button")
    public void user_clicks_on_login_button() {
        loginPage.clickLoginButton();
    }
    
    @Then("User should be redirected to products page")
    public void user_should_be_redirected_to_products_page() {
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), 
            "User is not on products page");
    }
    
    @Then("User should see {string} title")
    public void user_should_see_title(String expectedTitle) {
        String actualTitle = productsPage.getPageTitle();
        Assert.assertEquals(actualTitle, expectedTitle, 
            "Page title doesn't match. Expected: " + expectedTitle + ", Actual: " + actualTitle);
    }
    
    @Then("User should see error message containing {string}")
    public void user_should_see_error_message_containing(String expectedError) {
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message is not displayed");
        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(actualError.contains(expectedError), 
            "Error message doesn't contain expected text. Expected to contain: " + expectedError + 
            ", Actual: " + actualError);
    }
    
    @Then("User should remain on login page")
    public void user_should_remain_on_login_page() {
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
            "User is not on login page");
    }
    
    @Then("User should see {string}")
    public void user_should_see(String expectedText) {
        if (expectedText.equals("Products")) {
            Assert.assertTrue(productsPage.isProductsPageDisplayed(), 
                "Products page is not displayed");
        } else if (expectedText.contains("locked out") || expectedText.contains("do not match")) {
            String errorMessage = loginPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains(expectedText), 
                "Expected text not found in error message");
        }
    }
    
    @Given("User is logged in to the application")
    public void user_is_logged_in_to_the_application() {
        DriverManager.getDriver().get(config.getBaseUrl());
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), 
            "User login failed - Products page not displayed");
    }
}
