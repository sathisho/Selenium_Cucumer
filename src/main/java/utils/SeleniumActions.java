package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utility class containing common Selenium actions and waits
 */
public class SeleniumActions {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private ConfigReader config;
    
    public SeleniumActions(WebDriver driver) {
        this.driver = driver;
        this.config = new ConfigReader();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        this.actions = new Actions(driver);
    }
    
    // ========== WAITS ==========
    
    public WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    public void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    public void waitForTextPresent(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    // ========== CLICKS ==========
    
    public void click(By locator) {
        waitForClickable(locator).click();
    }
    
    public void jsClick(By locator) {
        WebElement element = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
    
    public void doubleClick(By locator) {
        WebElement element = waitForElement(locator);
        actions.doubleClick(element).perform();
    }
    
    public void rightClick(By locator) {
        WebElement element = waitForElement(locator);
        actions.contextClick(element).perform();
    }
    
    // ========== TEXT OPERATIONS ==========
    
    public void enterText(By locator, String text) {
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    public String getText(By locator) {
        return waitForElement(locator).getText();
    }
    
    public String getAttribute(By locator, String attribute) {
        return waitForElement(locator).getAttribute(attribute);
    }
    
    // ========== DROPDOWNS ==========
    
    public void selectByVisibleText(By locator, String text) {
        WebElement dropdown = driver.findElement(locator);
        Select select = new Select(dropdown);
        select.selectByVisibleText(text);
    }
    
    public void selectByValue(By locator, String value) {
        WebElement dropdown = driver.findElement(locator);
        Select select = new Select(dropdown);
        select.selectByValue(value);
    }
    
    public void selectByIndex(By locator, int index) {
        WebElement dropdown = driver.findElement(locator);
        Select select = new Select(dropdown);
        select.selectByIndex(index);
    }
    
    public List<String> getAllDropdownOptions(By locator) {
        WebElement dropdown = driver.findElement(locator);
        Select select = new Select(dropdown);
        List<String> options = new ArrayList<>();
        for (WebElement option : select.getOptions()) {
            options.add(option.getText());
        }
        return options;
    }
    
    // ========== CHECKBOXES & RADIO BUTTONS ==========
    
    public void checkCheckbox(By locator) {
        WebElement checkbox = driver.findElement(locator);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }
    
    public void uncheckCheckbox(By locator) {
        WebElement checkbox = driver.findElement(locator);
        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }
    
    public boolean isChecked(By locator) {
        return driver.findElement(locator).isSelected();
    }
    
    // ========== ALERTS ==========
    
    public void acceptAlert() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
    }
    
    public void dismissAlert() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.dismiss();
    }
    
    public String getAlertText() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        return alert.getText();
    }
    
    public void enterTextInAlert(String text) {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.sendKeys(text);
        alert.accept();
    }
    
    // ========== FRAMES ==========
    
    public void switchToFrame(By locator) {
        driver.switchTo().frame(driver.findElement(locator));
    }
    
    public void switchToFrameByIndex(int index) {
        driver.switchTo().frame(index);
    }
    
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }
    
    // ========== WINDOWS ==========
    
    public void switchToNewWindow() {
        String currentWindow = driver.getWindowHandle();
        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            if (!window.equals(currentWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
    }
    
    public void switchToWindowByTitle(String title) {
        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            driver.switchTo().window(window);
            if (driver.getTitle().equals(title)) {
                break;
            }
        }
    }
    
    public void closeCurrentWindow() {
        driver.close();
    }
    
    // ========== SCROLLING ==========
    
    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }
    
    public void scrollToTop() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0)");
    }
    
    // ========== MOUSE ACTIONS ==========
    
    public void hoverOverElement(By locator) {
        WebElement element = waitForElement(locator);
        actions.moveToElement(element).perform();
    }
    
    public void dragAndDrop(By source, By target) {
        WebElement sourceElement = driver.findElement(source);
        WebElement targetElement = driver.findElement(target);
        actions.dragAndDrop(sourceElement, targetElement).perform();
    }
    
    // ========== VERIFICATION ==========
    
    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    public boolean isEnabled(By locator) {
        return driver.findElement(locator).isEnabled();
    }
    
    // ========== LISTS ==========
    
    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }
    
    public int getElementCount(By locator) {
        return driver.findElements(locator).size();
    }
    
    // ========== FILE UPLOAD ==========
    
    public void uploadFile(By locator, String filePath) {
        driver.findElement(locator).sendKeys(filePath);
    }
}
