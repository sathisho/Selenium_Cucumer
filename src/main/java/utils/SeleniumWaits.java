package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

/**
 * Enhanced wait strategies for Selenium WebDriver.
 * Provides explicit waits, fluent waits, and custom wait conditions.
 */
public class SeleniumWaits {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final int WAIT_TIME = 10;
    private static final int POLLING_INTERVAL = 500; // milliseconds

    /**
     * Constructor to initialize wait with WebDriver
     */
    public SeleniumWaits(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
    }

    /**
     * Wait for element to be visible
     */
    public WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be visible with custom timeout
     */
    public WebElement waitForElementVisible(By locator, int timeoutInSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable
     */
    public WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be clickable with custom timeout
     */
    public WebElement waitForElementClickable(By locator, int timeoutInSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return customWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be present in DOM
     */
    public WebElement waitForElementPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for all elements to be visible
     */
    public List<WebElement> waitForAllElementsVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Wait for element to contain specific text
     */
    public boolean waitForElementText(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Wait for element to contain specific text with custom timeout
     */
    public boolean waitForElementText(By locator, String text, int timeoutInSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return customWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Wait for element value to contain specific text
     */
    public boolean waitForElementValue(By locator, String value) {
        return wait.until(ExpectedConditions.textToBePresentInElementValue(locator, value));
    }

    /**
     * Wait for element to be invisible/not visible
     */
    public boolean waitForElementInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be invisible with custom timeout
     */
    public boolean waitForElementInvisible(By locator, int timeoutInSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return customWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Fluent wait with custom polling interval
     */
    public WebElement fluentWait(By locator, int timeoutInSeconds, int pollingIntervalMs) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingIntervalMs))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Element not found after " + timeoutInSeconds + " seconds");
        return fluentWait.until(driver -> driver.findElement(locator));
    }

    /**
     * Wait for number of elements to equal expected count
     */
    public List<WebElement> waitForElementCount(By locator, int expectedCount) {
        wait.until(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));
        return driver.findElements(locator);
    }

    /**
     * Wait for number of elements to be greater than minimum
     */
    public List<WebElement> waitForElementCountGreaterThan(By locator, int minimumCount) {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, minimumCount));
        return driver.findElements(locator);
    }

    /**
     * Custom wait for URL to contain specific text
     */
    public boolean waitForUrlContains(String urlPortion) {
        return wait.until(ExpectedConditions.urlContains(urlPortion));
    }

    /**
     * Custom wait for page title to contain specific text
     */
    public boolean waitForTitleContains(String titlePortion) {
        return wait.until(ExpectedConditions.titleContains(titlePortion));
    }

    /**
     * Custom wait for element attribute to contain specific value
     */
    public boolean waitForElementAttribute(By locator, String attribute, String value) {
        return wait.until(driver -> {
            WebElement element = driver.findElement(locator);
            String attributeValue = element.getAttribute(attribute);
            return attributeValue != null && attributeValue.contains(value);
        });
    }

    /**
     * Wait for JavaScript to complete (jQuery AJAX calls)
     */
    public void waitForJavaScriptToLoad() {
        wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Wait for AJAX/jQuery to complete
     */
    public void waitForAjaxToComplete() {
        wait.until(driver -> (Boolean) ((JavascriptExecutor) driver)
                .executeScript("return (typeof jQuery != 'undefined') ? jQuery.active == 0 : true"));
    }

    /**
     * Wait for specific JavaScript condition to be true
     */
    public void waitForJavaScriptCondition(String jsCondition) {
        wait.until(driver -> (Boolean) ((JavascriptExecutor) driver).executeScript(jsCondition));
    }

    /**
     * Custom fluent wait for element with custom condition
     */
    public WebElement customFluentWait(By locator) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(WAIT_TIME))
                .pollingEvery(Duration.ofMillis(POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        return fluentWait.until(driver -> driver.findElement(locator));
    }

    /**
     * Wait for element to be selected
     */
    public boolean waitForElementSelected(By locator) {
        return wait.until(ExpectedConditions.elementSelectionStateToBe(locator, true));
    }

    /**
     * Wait for element to not be selected
     */
    public boolean waitForElementNotSelected(By locator) {
        return wait.until(ExpectedConditions.elementSelectionStateToBe(locator, false));
    }

    /**
     * Wait for alert to be present
     */
    public Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }
}
