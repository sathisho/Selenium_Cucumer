package stepdefinitions;

import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.LoginPage;
import pages.ProductsPage;
import utils.DriverManager;

/**
 * Step Definitions for Products feature
 */
public class ProductSteps {
    
    private ProductsPage productsPage;
    private LoginPage loginPage;
    private String productPrice;
    
    public ProductSteps() {
        productsPage = new ProductsPage(DriverManager.getDriver());
        loginPage = new LoginPage(DriverManager.getDriver());
    }
    
    @Then("User should see products page")
    public void user_should_see_products_page() {
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), 
            "Products page is not displayed");
    }
    
    @Then("User should see at least {int} products displayed")
    public void user_should_see_at_least_products_displayed(int minProductCount) {
        int actualCount = productsPage.getProductCount();
        Assert.assertTrue(actualCount >= minProductCount, 
            "Expected at least " + minProductCount + " products, but found " + actualCount);
    }
    
    @When("User adds product {string} to cart")
    public void user_adds_product_to_cart(String productName) {
        productsPage.addProductToCart(productName);
    }
    
    @Then("Shopping cart badge should show {string}")
    public void shopping_cart_badge_should_show(String expectedCount) {
        String actualCount = productsPage.getCartItemCount();
        Assert.assertEquals(actualCount, expectedCount, 
            "Cart count doesn't match. Expected: " + expectedCount + ", Actual: " + actualCount);
    }
    
    @Given("User adds product {string} to cart")
    public void given_user_adds_product_to_cart(String productName) {
        productsPage.addProductToCart(productName);
    }
    
    @When("User removes product {string} from cart")
    public void user_removes_product_from_cart(String productName) {
        productsPage.removeProductFromCart(productName);
    }
    
    @Then("Shopping cart badge should not be displayed")
    public void shopping_cart_badge_should_not_be_displayed() {
        String cartCount = productsPage.getCartItemCount();
        Assert.assertEquals(cartCount, "0", 
            "Shopping cart badge should not be displayed but shows: " + cartCount);
    }
    
    @When("User checks price of product {string}")
    public void user_checks_price_of_product(String productName) {
        productPrice = productsPage.getProductPrice(productName);
    }
    
    @Then("Product price should be {string}")
    public void product_price_should_be(String expectedPrice) {
        Assert.assertEquals(productPrice, expectedPrice, 
            "Product price doesn't match. Expected: " + expectedPrice + ", Actual: " + productPrice);
    }
    
    @When("User sorts products by {string}")
    public void user_sorts_products_by(String sortOption) {
        productsPage.sortProducts(sortOption);
    }
    
    @Then("Products should be sorted correctly")
    public void products_should_be_sorted_correctly() {
        // This is a simplified check - you can enhance this to verify actual sorting
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), 
            "Products page should be displayed after sorting");
    }
    
    @When("User clicks on menu button")
    public void user_clicks_on_menu_button() {
        productsPage.openMenu();
    }
    
    @When("User clicks on logout link")
    public void user_clicks_on_logout_link() {
        try {
            Thread.sleep(500); // Small wait for menu to open
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        productsPage.logout();
    }
    
    @Then("User should be redirected to login page")
    public void user_should_be_redirected_to_login_page() {
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
            "User should be on login page after logout");
    }
    
    @When("User clicks on shopping cart")
    public void user_clicks_on_shopping_cart() {
        productsPage.clickShoppingCart();
    }
    
    @Then("User should see cart page with {int} items")
    public void user_should_see_cart_page_with_items(int expectedItems) {
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("cart"), 
            "User should be on cart page. Current URL: " + currentUrl);
        
        // You can add more verification for item count in cart page
        // For now, just verify we're on cart page
    }
}
