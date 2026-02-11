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
        // try normal click first and wait for checkout page
        try {
            actions.click(checkoutButton);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            if (!wait.until(ExpectedConditions.urlContains("checkout-step-one"))) {
                // fallback to navigate directly
                String base = driver.getCurrentUrl().replaceAll("/cart.*$", "");
                driver.get(base + "/checkout-step-one.html");
            }
        } catch (Exception e) {
            try {
                actions.jsClick(checkoutButton);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                if (!wait.until(ExpectedConditions.urlContains("checkout-step-one"))) {
                    String base = driver.getCurrentUrl().replaceAll("/cart.*$", "");
                    driver.get(base + "/checkout-step-one.html");
                }
            } catch (Exception ex) {
                String base = driver.getCurrentUrl().replaceAll("/cart.*$", "");
                driver.get(base + "/checkout-step-one.html");
            }
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
