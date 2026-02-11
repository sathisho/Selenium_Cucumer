package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SeleniumActions;

/**
 * Page object for Checkout pages
 */
public class CheckoutPage {

    private WebDriver driver;
    private SeleniumActions actions;

    private By pageTitle = By.className("title");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new SeleniumActions(driver);
    }

    public boolean isCheckoutInformationPageDisplayed() {
        try {
            if (actions.isDisplayed(pageTitle)) {
                String title = actions.getText(pageTitle);
                return title != null && title.toLowerCase().contains("checkout");
            }
        } catch (Exception ignored) {}
        String currentUrl = driver.getCurrentUrl();
        return currentUrl != null && currentUrl.contains("checkout-step-one");
    }
}
