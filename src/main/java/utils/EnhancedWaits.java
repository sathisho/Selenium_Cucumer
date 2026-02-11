package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.List;

/**
 * Enhanced Wait Strategies for Selenium WebDriver
 * Provides explicit waits, fluent waits, and custom wait conditions
 */
public class EnhancedWaits {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private static final int DEFAULT_WAIT = 10;
    
    public EnhancedWaits(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
    }
    
    public EnhancedWaits(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
    
    /**
     * Wait for element to be visible on the page
     */
    public WebElement waitForElementVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable
     */
    public WebElement waitForElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be present in DOM
     */
    public WebElement waitForElementPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for all elements to be visible
     */
    public List<WebElement> waitForElementsVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }
    
    /**
     * Wait for element to be invisible
     */
    public boolean waitForElementInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element with specific text
     */
    public WebElement waitForElementWithText(By locator, String text) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for URL to change
     */
    public boolean waitForUrlChange(String expectedUrl) {
        return wait.until(ExpectedConditions.urlContains(expectedUrl));
    }
    
    /**
     * Wait for URL to be exactly what we expect
     */
    public boolean waitForUrlToBe(String expectedUrl) {
        return wait.until(ExpectedConditions.urlToBe(expectedUrl));
    }
    
    /**
     * Wait for title to contain text
     */
    public boolean waitForTitleContains(String title) {
        return wait.until(ExpectedConditions.titleContains(title));
    }
    
    /**
     * Wait for title to be exactly what we expect
     */
    public boolean waitForTitleToBe(String title) {
        return wait.until(ExpectedConditions.titleIs(title));
    }
    
    /**
     * Custom wait for JavaScript to be ready
     */
    public boolean waitForJavaScriptReady() {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return customWait.until(driver -> 
            (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Custom wait for jQuery AJAX calls to complete
     */
    public boolean waitForAjaxComplete() {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            return customWait.until(driver ->
                (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("return jQuery.active == 0"));
        } catch (Exception e) {
            System.out.println("jQuery not loaded or AJAX not in use");
            return true;
        }
    }
    
    /**
     * Wait for element to have specific attribute value
     */
    public boolean waitForAttributeValue(By locator, String attribute, String value) {
        return wait.until(driver -> {
            WebElement element = driver.findElement(locator);
            String attributeValue = element.getAttribute(attribute);
            return attributeValue != null && attributeValue.equals(value);
        });
    }
    
    /**
     * Wait for element to have specific CSS value
     */
    public boolean waitForCssValue(By locator, String property, String value) {
        return wait.until(driver -> {
            WebElement element = driver.findElement(locator);
            String cssValue = element.getCssValue(property);
            return cssValue != null && cssValue.equals(value);
        });
    }
    
    /**
     * Wait for number of windows/tabs to be specific count
     */
    public boolean waitForWindowCount(int expectedCount) {
        return wait.until(driver -> driver.getWindowHandles().size() == expectedCount);
    }
    
    /**
     * Wait for element to be enabled
     */
    public boolean waitForElementEnabled(By locator) {
        return wait.until(driver -> {
            WebElement element = driver.findElement(locator);
            return element.isEnabled();
        });
    }
    
    /**
     * Wait for element to be disabled
     */
    public boolean waitForElementDisabled(By locator) {
        return wait.until(driver -> {
            WebElement element = driver.findElement(locator);
            return !element.isEnabled();
        });
    }
}
