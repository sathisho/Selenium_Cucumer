package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.SeleniumActions;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object Model for Products Page
 */
public class ProductsPage {
    
    private WebDriver driver;
    private SeleniumActions actions;
    
    // Locators
    private By pageTitle = By.className("title");
    private By productItems = By.className("inventory_item");
    private By shoppingCartBadge = By.className("shopping_cart_badge");
    private By shoppingCartLink = By.className("shopping_cart_link");
    private By menuButton = By.id("react-burger-menu-btn");
    private By logoutLink = By.id("logout_sidebar_link");
    private By productSortDropdown = By.className("product_sort_container");
    
    // Dynamic locators
    private String addToCartButtonXPath = "//div[text()='%s']/ancestor::div[@class='inventory_item']//button[text()='Add to cart']";
    private String removeButtonXPath = "//div[text()='%s']/ancestor::div[@class='inventory_item']//button[text()='Remove']";
    private String productPriceXPath = "//div[text()='%s']/ancestor::div[@class='inventory_item']//div[@class='inventory_item_price']";
    
    // Constructor
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new SeleniumActions(driver);
    }
    
    // Verifications
    public boolean isProductsPageDisplayed() {
        return actions.isDisplayed(pageTitle) && 
               actions.getText(pageTitle).equals("Products");
    }
    
    public String getPageTitle() {
        return actions.getText(pageTitle);
    }
    
    // Product operations
    public int getProductCount() {
        return actions.getElementCount(productItems);
    }
    
    public void addProductToCart(String productName) {
        By addToCartButton = By.xpath(String.format(addToCartButtonXPath, productName));
        actions.click(addToCartButton);
    }
    
    public void removeProductFromCart(String productName) {
        By removeButton = By.xpath(String.format(removeButtonXPath, productName));
        actions.click(removeButton);
    }
    
    public String getProductPrice(String productName) {
        By priceLocator = By.xpath(String.format(productPriceXPath, productName));
        return actions.getText(priceLocator);
    }
    
    public boolean isProductDisplayed(String productName) {
        try {
            By productLocator = By.xpath(String.format("//div[text()='%s']", productName));
            return actions.isDisplayed(productLocator);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Cart operations
    public String getCartItemCount() {
        if (actions.isDisplayed(shoppingCartBadge)) {
            return actions.getText(shoppingCartBadge);
        }
        return "0";
    }
    
    public void clickShoppingCart() {
        try {
            actions.click(shoppingCartLink);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlContains("cart"));
        } catch (Exception e) {
            try {
                WebElement link = driver.findElement(shoppingCartLink);
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    driver.get(href);
                } else {
                    actions.jsClick(shoppingCartLink);
                }
            } catch (Exception ex) {
                String base = driver.getCurrentUrl().replaceAll("/inventory.*$", "");
                driver.get(base + "/cart.html");
            }
        }
    }
    
    // Sort operations
    public void sortProducts(String sortOption) {
        actions.selectByVisibleText(productSortDropdown, sortOption);
    }
    
    // Menu operations
    public void openMenu() {
        actions.jsClick(menuButton);
    }
    
    public void logout() {
        openMenu();
        actions.click(logoutLink);
    }
}
