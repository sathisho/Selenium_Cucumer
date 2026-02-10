package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SeleniumActions;

/**
 * Page Object Model for Login Page
 */
public class LoginPage {
    
    private WebDriver driver;
    private SeleniumActions actions;
    
    // Locators
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.cssSelector("[data-test='error']");
    private By logoImage = By.className("login_logo");
    
    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new SeleniumActions(driver);
    }
    
    // Actions
    public void enterUsername(String username) {
        actions.enterText(usernameField, username);
    }
    
    public void enterPassword(String password) {
        actions.enterText(passwordField, password);
    }
    
    public void clickLoginButton() {
        actions.click(loginButton);
    }
    
    public String getErrorMessage() {
        return actions.getText(errorMessage);
    }
    
    public boolean isErrorDisplayed() {
        return actions.isDisplayed(errorMessage);
    }
    
    public boolean isLoginPageDisplayed() {
        return actions.isDisplayed(logoImage);
    }
    
    // Combined action
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
