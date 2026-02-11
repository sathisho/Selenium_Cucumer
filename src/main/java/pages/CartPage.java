package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.SeleniumActions;

import java.time.Duration;

/**
 * Page object for Cart page
 */
public class CartPage {

    private WebDriver driver;
    private SeleniumActions actions;

    private By checkoutButton = By.id("checkout");
    private By cartTitle = By.className("title");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new SeleniumActions(driver);
    }

    public void clickCheckout() {
        // try normal click first
        try {
            actions.click(checkoutButton);
        } catch (Exception e) {
            actions.jsClick(checkoutButton);
        }
    }

    public boolean isCartPageDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return wait.until(ExpectedConditions.urlContains("cart"));
        } catch (Exception e) {
            return false;
        }
    }
}
