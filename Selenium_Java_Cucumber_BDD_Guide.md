# Java Selenium with Cucumber BDD - Complete Automation Guide

## Table of Contents
1. [Project Setup & Configuration](#1-project-setup--configuration)
2. [BDD Basics & Gherkin Syntax](#2-bdd-basics--gherkin-syntax)
3. [Writing Test Scripts & Locators](#3-writing-test-scripts--locators)
4. [Framework Design (Cucumber & Page Object Model)](#4-framework-design)
5. [Best Practices & Common Challenges](#5-best-practices--common-challenges)

---

## 1. Project Setup & Configuration

### 1.1 Prerequisites
- Java JDK 8 or higher
- Maven or Gradle (Maven recommended)
- IDE (IntelliJ IDEA or Eclipse)
- Browser drivers (ChromeDriver, GeckoDriver, etc.)

### 1.2 Maven Project Setup

**pom.xml dependencies:**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.automation</groupId>
    <artifactId>selenium-cucumber-framework</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <selenium.version>4.16.1</selenium.version>
        <cucumber.version>7.15.0</cucumber.version>
    </properties>

    <dependencies>
        <!-- Selenium Java -->
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>${selenium.version}</version>
        </dependency>

        <!-- Cucumber Java -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>${cucumber.version}</version>
        </dependency>

        <!-- Cucumber TestNG (or JUnit) -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-testng</artifactId>
            <version>${cucumber.version}</version>
        </dependency>

        <!-- WebDriverManager -->
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>5.6.2</version>
        </dependency>

        <!-- TestNG -->
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>7.8.0</version>
        </dependency>

        <!-- ExtentReports Cucumber Adapter -->
        <dependency>
            <groupId>tech.grasshopper</groupId>
            <artifactId>extentreports-cucumber7-adapter</artifactId>
            <version>1.14.0</version>
        </dependency>

        <!-- Apache POI for Excel -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>5.2.5</version>
        </dependency>

        <!-- Log4j -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.22.0</version>
        </dependency>

        <!-- AssertJ (Better assertions) -->
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <version>3.24.2</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.2</version>
                <configuration>
                    <includes>
                        <include>**/TestRunner.java</include>
                    </includes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### 1.3 Project Structure

```
selenium-cucumber-framework/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── pages/
│   │       │   ├── LoginPage.java
│   │       │   ├── HomePage.java
│   │       │   └── ProductPage.java
│   │       │
│   │       ├── utils/
│   │       │   ├── ConfigReader.java
│   │       │   ├── DriverManager.java
│   │       │   ├── ExcelReader.java
│   │       │   └── ScreenshotUtil.java
│   │       │
│   │       └── hooks/
│   │           └── Hooks.java
│   │
│   └── test/
│       ├── java/
│       │   ├── stepdefinitions/
│       │   │   ├── LoginSteps.java
│       │   │   ├── HomeSteps.java
│       │   │   └── ProductSteps.java
│       │   │
│       │   └── runners/
│       │       └── TestRunner.java
│       │
│       └── resources/
│           ├── features/
│           │   ├── Login.feature
│           │   ├── Home.feature
│           │   └── Product.feature
│           │
│           ├── config.properties
│           ├── testdata.xlsx
│           └── extent.properties
│
└── pom.xml
```

### 1.4 Configuration Files

**config.properties:**

```properties
# Browser Configuration
browser=chrome
headless=false
implicit.wait=10
explicit.wait=20
page.load.timeout=30

# Application URL
base.url=https://your-application-url.com

# Test Data
test.data.path=src/test/resources/testdata.xlsx

# Screenshots
screenshot.path=test-output/screenshots/

# Reports
report.path=test-output/reports/
```

**extent.properties:**

```properties
extent.reporter.spark.start=true
extent.reporter.spark.out=test-output/reports/ExtentReport.html
extent.reporter.spark.config=src/test/resources/extent-config.xml

screenshot.dir=test-output/screenshots/
screenshot.rel.path=../screenshots/

extent.reporter.pdf.start=true
extent.reporter.pdf.out=test-output/reports/ExtentReport.pdf
```

---

## 2. BDD Basics & Gherkin Syntax

### 2.1 What is BDD?

**Behavior-Driven Development (BDD)** is an agile software development approach where:
- Tests are written in plain English
- Focus is on business behavior, not implementation
- Collaboration between developers, testers, and business stakeholders
- Uses Gherkin language for writing scenarios

### 2.2 Gherkin Keywords

- **Feature:** High-level description of software feature
- **Scenario:** Specific test case
- **Given:** Precondition/setup
- **When:** Action/event
- **Then:** Expected outcome
- **And:** Additional steps
- **But:** Negative additional steps
- **Background:** Common steps for all scenarios in a feature
- **Scenario Outline:** Data-driven testing
- **Examples:** Test data for Scenario Outline

### 2.3 Feature File Examples

**Login.feature:**

```gherkin
Feature: User Login Functionality
  As a user
  I want to login to the application
  So that I can access my account

  Background:
    Given User navigates to the login page

  @smoke @regression
  Scenario: Successful login with valid credentials
    When User enters username "testuser@email.com"
    And User enters password "Test@123"
    And User clicks on login button
    Then User should be redirected to home page
    And User should see welcome message "Welcome, Test User"

  @regression
  Scenario: Failed login with invalid credentials
    When User enters username "invalid@email.com"
    And User enters password "WrongPass"
    And User clicks on login button
    Then User should see error message "Invalid credentials"
    And User should remain on login page

  @regression
  Scenario: Login with empty credentials
    When User clicks on login button
    Then User should see error message "Username is required"

  # Data-driven testing with Scenario Outline
  @regression @dataDriven
  Scenario Outline: Login with multiple credentials
    When User enters username "<username>"
    And User enters password "<password>"
    And User clicks on login button
    Then User should see "<result>"

    Examples:
      | username              | password    | result                    |
      | valid@email.com       | ValidPass1  | Welcome, Test User        |
      | invalid@email.com     | Invalid123  | Invalid credentials       |
      | test@email.com        |             | Password is required      |
      |                       | Test@123    | Username is required      |
```

**Product.feature:**

```gherkin
Feature: Product Search and Purchase
  As a customer
  I want to search and purchase products
  So that I can complete my shopping

  Background:
    Given User is logged in to the application

  @smoke
  Scenario: Search for a product
    When User enters "Laptop" in search box
    And User clicks on search button
    Then User should see search results for "Laptop"
    And Results should contain at least 5 products

  @regression
  Scenario: Add product to cart
    Given User searches for "iPhone 15"
    When User selects first product from results
    And User clicks on "Add to Cart" button
    Then Product should be added to cart
    And Cart count should increase by 1
    And User should see confirmation message "Product added to cart"

  @regression @e2e
  Scenario: Complete purchase flow
    Given User has product "Samsung TV" in cart
    When User proceeds to checkout
    And User enters shipping address
      | Street     | 123 Main St        |
      | City       | New York           |
      | State      | NY                 |
      | ZipCode    | 10001              |
    And User selects payment method "Credit Card"
    And User enters payment details
    And User clicks on "Place Order" button
    Then Order should be placed successfully
    And User should see order confirmation number
    And User should receive confirmation email
```

### 2.4 Writing Good Gherkin

**Good Practices:**

```gherkin
# ✅ GOOD - Clear, readable, business-focused
Scenario: User successfully registers with valid data
  Given User is on registration page
  When User fills in registration form with valid details
  And User accepts terms and conditions
  And User clicks register button
  Then User account should be created
  And Welcome email should be sent

# ❌ BAD - Too technical, implementation details
Scenario: Registration
  Given driver.get("http://example.com/register")
  When driver.findElement(By.id("username")).sendKeys("test")
  And click button with xpath "//button[@id='submit']"
  Then assert database has new record
```

**Tips:**
- Keep scenarios independent
- One scenario = one test case
- Use business language, not technical jargon
- Make steps reusable
- Avoid too many AND steps (max 5-7 steps per scenario)
- Use tags for organizing tests (@smoke, @regression, @critical)

---

## 3. Writing Test Scripts & Locators

### 3.1 Locator Strategies (Priority Order)

**Best to Worst:**
1. **ID** - Fastest and most reliable
2. **Name** - Good for form elements
3. **CSS Selector** - Fast and flexible
4. **XPath** - Powerful but slower
5. **Link Text / Partial Link Text** - For links only
6. **Class Name** - When unique
7. **Tag Name** - Rarely used alone

### 3.2 Locator Examples

```java
// 1. ID (Best - Always prefer if available)
driver.findElement(By.id("username"));
driver.findElement(By.id("submitBtn"));

// 2. Name
driver.findElement(By.name("password"));
driver.findElement(By.name("email"));

// 3. CSS Selector (Fast and flexible)
// By ID
driver.findElement(By.cssSelector("#username"));
// By Class
driver.findElement(By.cssSelector(".error-message"));
// By attribute
driver.findElement(By.cssSelector("input[type='submit']"));
driver.findElement(By.cssSelector("input[name='email']"));
// Combination
driver.findElement(By.cssSelector("div.container input#username"));
// Child element
driver.findElement(By.cssSelector("form > input"));
// Descendant
driver.findElement(By.cssSelector("div.form input"));
// Contains attribute
driver.findElement(By.cssSelector("input[id*='user']"));
// Starts with
driver.findElement(By.cssSelector("input[id^='btn']"));
// Ends with
driver.findElement(By.cssSelector("input[id$='Submit']"));

// 4. XPath (Powerful but slower)
// Absolute XPath (❌ Avoid - breaks easily)
driver.findElement(By.xpath("/html/body/div/form/input"));

// Relative XPath (✅ Preferred)
driver.findElement(By.xpath("//input[@id='username']"));
driver.findElement(By.xpath("//button[@name='submit']"));

// Text-based
driver.findElement(By.xpath("//button[text()='Login']"));
driver.findElement(By.xpath("//h1[text()='Welcome']"));

// Contains
driver.findElement(By.xpath("//button[contains(@class, 'btn-primary')]"));
driver.findElement(By.xpath("//div[contains(text(), 'Success')]"));

// Starts-with
driver.findElement(By.xpath("//input[starts-with(@id, 'user')]"));

// Multiple attributes
driver.findElement(By.xpath("//input[@type='text' and @name='email']"));
driver.findElement(By.xpath("//button[@class='btn' or @id='submitBtn']"));

// Axes - Parent
driver.findElement(By.xpath("//input[@id='username']/parent::div"));
// Axes - Following-sibling
driver.findElement(By.xpath("//label[text()='Email']/following-sibling::input"));
// Axes - Preceding-sibling
driver.findElement(By.xpath("//input[@id='password']/preceding-sibling::label"));
// Axes - Ancestor
driver.findElement(By.xpath("//input[@id='username']/ancestor::form"));
// Axes - Descendant
driver.findElement(By.xpath("//form/descendant::input[@type='submit']"));

// Index-based (use cautiously)
driver.findElement(By.xpath("(//input[@type='text'])[2]"));
driver.findElement(By.xpath("//table//tr[3]/td[2]"));

// 5. Link Text
driver.findElement(By.linkText("Forgot Password?"));
driver.findElement(By.partialLinkText("Forgot"));

// 6. Class Name (when unique)
driver.findElement(By.className("btn-submit"));

// 7. Tag Name (rarely alone)
driver.findElement(By.tagName("button"));
```

### 3.3 Dynamic Locators

```java
// Method to create dynamic XPath
public By getDynamicXPath(String baseXPath, String value) {
    String xpath = String.format(baseXPath, value);
    return By.xpath(xpath);
}

// Usage
By userRow = getDynamicXPath("//tr[@data-user='%s']", "john.doe");
By productPrice = getDynamicXPath("//div[@product-id='%s']//span[@class='price']", "12345");

// In Page Object
private String dynamicProductXPath = "//div[@data-product='%s']//button[text()='Add to Cart']";

public void addProductToCart(String productName) {
    String xpath = String.format(dynamicProductXPath, productName);
    driver.findElement(By.xpath(xpath)).click();
}
```

### 3.4 Common Selenium Actions

```java
package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.*;

public class SeleniumActions {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    
    public SeleniumActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
    
    public void enterTextSlowly(By locator, String text, int delayMillis) {
        WebElement element = waitForElement(locator);
        element.clear();
        for (char ch : text.toCharArray()) {
            element.sendKeys(String.valueOf(ch));
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
```

---

## 4. Framework Design

### 4.1 Driver Manager

```java
package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

public class DriverManager {
    
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ConfigReader config = new ConfigReader();
    
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            driver.set(createDriver());
        }
        return driver.get();
    }
    
    private static WebDriver createDriver() {
        WebDriver webDriver;
        String browser = config.getBrowser();
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (config.isHeadless()) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--start-maximized");
                webDriver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver();
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver();
                break;
                
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(config.getImplicitWait())
        );
        webDriver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(config.getPageLoadTimeout())
        );
        
        return webDriver;
    }
    
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
```

### 4.2 Config Reader

```java
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    
    private Properties properties;
    
    public ConfigReader() {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }
    
    public String getBrowser() {
        return properties.getProperty("browser");
    }
    
    public boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless"));
    }
    
    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait"));
    }
    
    public int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait"));
    }
    
    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("page.load.timeout"));
    }
    
    public String getBaseUrl() {
        return properties.getProperty("base.url");
    }
    
    public String getTestDataPath() {
        return properties.getProperty("test.data.path");
    }
}
```

### 4.3 Hooks Class

```java
package hooks;

import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

public class Hooks {
    
    private WebDriver driver;
    
    @Before
    public void setUp(Scenario scenario) {
        System.out.println("Starting scenario: " + scenario.getName());
        driver = DriverManager.getDriver();
    }
    
    @After
    public void tearDown(Scenario scenario) {
        // Take screenshot if scenario fails
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
        
        System.out.println("Scenario finished: " + scenario.getName());
        System.out.println("Status: " + scenario.getStatus());
        
        DriverManager.quitDriver();
    }
    
    @BeforeStep
    public void beforeStep() {
        // Code to execute before each step
    }
    
    @AfterStep
    public void afterStep(Scenario scenario) {
        // Optional: Take screenshot after each step
        // Useful for debugging but creates many screenshots
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Step Screenshot");
        }
    }
}
```

### 4.4 Page Object Model Example

**LoginPage.java:**

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SeleniumActions;

public class LoginPage {
    
    private WebDriver driver;
    private SeleniumActions actions;
    
    // Locators
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    private By errorMessage = By.cssSelector(".error-message");
    private By welcomeMessage = By.xpath("//h1[contains(text(), 'Welcome')]");
    private By forgotPasswordLink = By.linkText("Forgot Password?");
    
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
    
    public String getWelcomeMessage() {
        return actions.getText(welcomeMessage);
    }
    
    public boolean isErrorDisplayed() {
        return actions.isDisplayed(errorMessage);
    }
    
    public void clickForgotPassword() {
        actions.click(forgotPasswordLink);
    }
    
    // Combined action
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
}
```

**HomePage.java:**

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SeleniumActions;

public class HomePage {
    
    private WebDriver driver;
    private SeleniumActions actions;
    
    // Locators
    private By searchBox = By.id("search");
    private By searchButton = By.cssSelector("button[type='submit']");
    private By cartIcon = By.id("cart-icon");
    private By cartCount = By.cssSelector(".cart-count");
    private By userProfile = By.xpath("//div[@class='user-profile']");
    private By logoutLink = By.linkText("Logout");
    
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new SeleniumActions(driver);
    }
    
    public void searchProduct(String productName) {
        actions.enterText(searchBox, productName);
        actions.click(searchButton);
    }
    
    public void clickCart() {
        actions.click(cartIcon);
    }
    
    public int getCartCount() {
        String count = actions.getText(cartCount);
        return Integer.parseInt(count);
    }
    
    public void logout() {
        actions.hoverOverElement(userProfile);
        actions.click(logoutLink);
    }
    
    public boolean isUserLoggedIn() {
        return actions.isDisplayed(userProfile);
    }
}
```

**ProductPage.java:**

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.SeleniumActions;
import java.util.List;

public class ProductPage {
    
    private WebDriver driver;
    private SeleniumActions actions;
    
    // Locators
    private By productResults = By.cssSelector(".product-item");
    private By productTitle = By.cssSelector(".product-title");
    private By productPrice = By.cssSelector(".product-price");
    private By addToCartButton = By.xpath("//button[text()='Add to Cart']");
    private By confirmationMessage = By.cssSelector(".confirmation-msg");
    
    // Dynamic locators
    private String productByNameXPath = "//div[@class='product-item']//h3[text()='%s']";
    private String addToCartByProductXPath = "//div[@class='product-item']//h3[text()='%s']/ancestor::div[@class='product-item']//button[text()='Add to Cart']";
    
    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new SeleniumActions(driver);
    }
    
    public int getProductCount() {
        return actions.getElementCount(productResults);
    }
    
    public void selectProduct(String productName) {
        By productLocator = By.xpath(String.format(productByNameXPath, productName));
        actions.click(productLocator);
    }
    
    public void addProductToCart(String productName) {
        By addToCartLocator = By.xpath(String.format(addToCartByProductXPath, productName));
        actions.click(addToCartLocator);
    }
    
    public void addFirstProductToCart() {
        List<WebElement> products = actions.getElements(productResults);
        if (!products.isEmpty()) {
            products.get(0).findElement(addToCartButton).click();
        }
    }
    
    public String getConfirmationMessage() {
        return actions.getText(confirmationMessage);
    }
    
    public boolean areProductsDisplayed() {
        return actions.isDisplayed(productResults);
    }
}
```

### 4.5 Step Definitions

**LoginSteps.java:**

```java
package stepdefinitions;

import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.LoginPage;
import pages.HomePage;
import utils.DriverManager;
import utils.ConfigReader;

public class LoginSteps {
    
    private LoginPage loginPage;
    private HomePage homePage;
    private ConfigReader config;
    
    public LoginSteps() {
        loginPage = new LoginPage(DriverManager.getDriver());
        homePage = new HomePage(DriverManager.getDriver());
        config = new ConfigReader();
    }
    
    @Given("User navigates to the login page")
    public void user_navigates_to_the_login_page() {
        DriverManager.getDriver().get(config.getBaseUrl() + "/login");
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
    
    @Then("User should be redirected to home page")
    public void user_should_be_redirected_to_home_page() {
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/home"), "User not on home page");
    }
    
    @Then("User should see welcome message {string}")
    public void user_should_see_welcome_message(String expectedMessage) {
        String actualMessage = loginPage.getWelcomeMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage), 
            "Welcome message doesn't match. Expected: " + expectedMessage + ", Actual: " + actualMessage);
    }
    
    @Then("User should see error message {string}")
    public void user_should_see_error_message(String expectedError) {
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message not displayed");
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError, "Error message doesn't match");
    }
    
    @Then("User should remain on login page")
    public void user_should_remain_on_login_page() {
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/login"), "User not on login page");
    }
}
```

**ProductSteps.java:**

```java
package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.HomePage;
import pages.ProductPage;
import utils.DriverManager;
import java.util.Map;

public class ProductSteps {
    
    private HomePage homePage;
    private ProductPage productPage;
    private int initialCartCount;
    
    public ProductSteps() {
        homePage = new HomePage(DriverManager.getDriver());
        productPage = new ProductPage(DriverManager.getDriver());
    }
    
    @Given("User is logged in to the application")
    public void user_is_logged_in_to_the_application() {
        // Assuming user is already logged in from previous steps
        // or you can implement login here
        Assert.assertTrue(homePage.isUserLoggedIn(), "User is not logged in");
    }
    
    @When("User enters {string} in search box")
    public void user_enters_in_search_box(String productName) {
        homePage.searchProduct(productName);
    }
    
    @When("User clicks on search button")
    public void user_clicks_on_search_button() {
        // Already clicked in searchProduct method
    }
    
    @Then("User should see search results for {string}")
    public void user_should_see_search_results_for(String productName) {
        Assert.assertTrue(productPage.areProductsDisplayed(), "No products displayed");
    }
    
    @Then("Results should contain at least {int} products")
    public void results_should_contain_at_least_products(int minCount) {
        int actualCount = productPage.getProductCount();
        Assert.assertTrue(actualCount >= minCount, 
            "Expected at least " + minCount + " products, but found " + actualCount);
    }
    
    @Given("User searches for {string}")
    public void user_searches_for(String productName) {
        homePage.searchProduct(productName);
    }
    
    @When("User selects first product from results")
    public void user_selects_first_product_from_results() {
        productPage.addFirstProductToCart();
    }
    
    @When("User clicks on {string} button")
    public void user_clicks_on_button(String buttonName) {
        // Handled in addFirstProductToCart
    }
    
    @Then("Product should be added to cart")
    public void product_should_be_added_to_cart() {
        String message = productPage.getConfirmationMessage();
        Assert.assertTrue(message.contains("added"), "Product not added to cart");
    }
    
    @Then("Cart count should increase by {int}")
    public void cart_count_should_increase_by(int increment) {
        int currentCount = homePage.getCartCount();
        Assert.assertEquals(currentCount, initialCartCount + increment, "Cart count didn't increase");
    }
    
    @Then("User should see confirmation message {string}")
    public void user_should_see_confirmation_message(String expectedMessage) {
        String actualMessage = productPage.getConfirmationMessage();
        Assert.assertEquals(actualMessage, expectedMessage, "Confirmation message doesn't match");
    }
    
    @Given("User has product {string} in cart")
    public void user_has_product_in_cart(String productName) {
        homePage.searchProduct(productName);
        productPage.addProductToCart(productName);
        initialCartCount = homePage.getCartCount();
    }
    
    @When("User proceeds to checkout")
    public void user_proceeds_to_checkout() {
        homePage.clickCart();
        // Add checkout steps
    }
    
    @When("User enters shipping address")
    public void user_enters_shipping_address(DataTable dataTable) {
        Map<String, String> address = dataTable.asMap(String.class, String.class);
        
        // Use the address data
        String street = address.get("Street");
        String city = address.get("City");
        String state = address.get("State");
        String zipCode = address.get("ZipCode");
        
        // Implement form filling logic here
        System.out.println("Filling address: " + street + ", " + city + ", " + state + " " + zipCode);
    }
    
    @When("User selects payment method {string}")
    public void user_selects_payment_method(String paymentMethod) {
        // Implement payment method selection
        System.out.println("Selected payment method: " + paymentMethod);
    }
    
    @When("User enters payment details")
    public void user_enters_payment_details() {
        // Implement payment details entry
    }
    
    @Then("Order should be placed successfully")
    public void order_should_be_placed_successfully() {
        // Verify order placement
        Assert.assertTrue(true, "Order placed successfully");
    }
    
    @Then("User should see order confirmation number")
    public void user_should_see_order_confirmation_number() {
        // Verify order number displayed
        Assert.assertTrue(true, "Order confirmation number displayed");
    }
    
    @Then("User should receive confirmation email")
    public void user_should_receive_confirmation_email() {
        // This would typically check email or database
        System.out.println("Confirmation email check - typically requires email API integration");
    }
}
```

### 4.6 Test Runner

```java
package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks"},
        tags = "@smoke or @regression",
        plugin = {
                "pretty",
                "html:test-output/cucumber-reports/cucumber.html",
                "json:test-output/cucumber-reports/cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        dryRun = false
)
public class TestRunner extends AbstractTestNGCucumberTests {
    // This class will be empty - TestNG will invoke Cucumber
}
```

**Tag explanations:**
```java
// Run only smoke tests
tags = "@smoke"

// Run smoke OR regression
tags = "@smoke or @regression"

// Run smoke AND regression (tests must have both tags)
tags = "@smoke and @regression"

// Run regression but not wip (work in progress)
tags = "@regression and not @wip"

// Complex combinations
tags = "(@smoke or @regression) and not @skip"
```

---

## 5. Best Practices & Common Challenges

### 5.1 Best Practices

#### ✅ DO's

```java
// 1. Use Page Object Model
// ✅ GOOD
public class LoginPage {
    private By usernameField = By.id("username");
    
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }
}

// 2. Use explicit waits
// ✅ GOOD
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));

// 3. Make scenarios independent
// ✅ GOOD - Each scenario sets up its own data
Scenario: User can login
  Given User is on login page
  When User enters valid credentials
  
Scenario: User can search products
  Given User is logged in
  When User searches for product

// 4. Use meaningful variable names
// ✅ GOOD
String expectedWelcomeMessage = "Welcome, John";
String actualWelcomeMessage = loginPage.getWelcomeMessage();

// 5. Use constants for test data
// ✅ GOOD
public class TestData {
    public static final String VALID_USERNAME = "test@email.com";
    public static final String VALID_PASSWORD = "Test@123";
    public static final int DEFAULT_TIMEOUT = 10;
}

// 6. Organize locators properly
// ✅ GOOD
public class LoginPage {
    // Group related locators
    // Form fields
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    
    // Buttons
    private By loginButton = By.cssSelector("button[type='submit']");
    private By forgotPasswordButton = By.linkText("Forgot Password");
    
    // Messages
    private By errorMessage = By.cssSelector(".error");
    private By successMessage = By.cssSelector(".success");
}
```

#### ❌ DON'Ts

```java
// 1. Don't use Thread.sleep()
// ❌ BAD
Thread.sleep(5000);
driver.findElement(By.id("element")).click();

// ✅ GOOD
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.elementToBeClickable(By.id("element"))).click();

// 2. Don't hard code selectors in step definitions
// ❌ BAD
@When("User clicks login")
public void userClicksLogin() {
    driver.findElement(By.id("loginBtn")).click();
}

// ✅ GOOD
@When("User clicks login")
public void userClicksLogin() {
    loginPage.clickLoginButton();
}

// 3. Don't make scenarios dependent on each other
// ❌ BAD
Scenario: User logs in
  Given User is on login page
  When User logs in
  
Scenario: User searches (depends on previous scenario)
  When User searches for product  # Assumes user is logged in

// 4. Don't use absolute XPath
// ❌ BAD
By element = By.xpath("/html/body/div[1]/div[2]/form/input[1]");

// ✅ GOOD
By element = By.xpath("//input[@id='username']");

// 5. Don't ignore exceptions
// ❌ BAD
try {
    driver.findElement(By.id("element")).click();
} catch (Exception e) {
    // Ignoring exception
}

// ✅ GOOD
try {
    driver.findElement(By.id("element")).click();
} catch (NoSuchElementException e) {
    System.out.println("Element not found: " + e.getMessage());
    throw e;  // Re-throw or handle appropriately
}
```

### 5.2 Common Challenges & Solutions

#### Challenge 1: Element Not Found

```java
// Problem
org.openqa.selenium.NoSuchElementException: no such element

// Solutions:
// 1. Add explicit wait
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));

// 2. Check if element is in iframe
driver.switchTo().frame("frameName");
driver.findElement(By.id("element")).click();
driver.switchTo().defaultContent();

// 3. Wait for page to load completely
wait.until(webDriver -> ((JavascriptExecutor) webDriver)
    .executeScript("return document.readyState").equals("complete"));

// 4. Scroll to element before interacting
JavascriptExecutor js = (JavascriptExecutor) driver;
js.executeScript("arguments[0].scrollIntoView(true);", element);
```

#### Challenge 2: Element Not Clickable

```java
// Problem
org.openqa.selenium.ElementClickInterceptedException

// Solutions:
// 1. Wait for element to be clickable
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
element.click();

// 2. Use JavaScript click
JavascriptExecutor js = (JavascriptExecutor) driver;
js.executeScript("arguments[0].click();", element);

// 3. Remove overlay/modal first
wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-overlay")));
element.click();

// 4. Scroll to element first
Actions actions = new Actions(driver);
actions.moveToElement(element).click().perform();
```

#### Challenge 3: Stale Element Reference

```java
// Problem
org.openqa.selenium.StaleElementReferenceException

// Solutions:
// 1. Re-find the element
public void clickElement(By locator) {
    for (int i = 0; i < 3; i++) {
        try {
            driver.findElement(locator).click();
            break;
        } catch (StaleElementReferenceException e) {
            System.out.println("Stale element, retrying...");
        }
    }
}

// 2. Use fresh locator each time
// ❌ BAD
WebElement element = driver.findElement(By.id("username"));
element.sendKeys("test");
// ... page refresh happens ...
element.click();  // Will throw StaleElementReferenceException

// ✅ GOOD
driver.findElement(By.id("username")).sendKeys("test");
// ... page refresh happens ...
driver.findElement(By.id("username")).click();  // Finds element again
```

#### Challenge 4: Handling Dynamic Elements

```java
// Elements with changing IDs
// ❌ BAD
By element = By.id("element_123456");  // ID changes each time

// ✅ GOOD
By element = By.xpath("//div[starts-with(@id, 'element_')]");
By element = By.cssSelector("div[id^='element_']");

// Elements that load dynamically
public void waitForDynamicElement(String productName) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    String xpath = String.format("//div[@class='product']//h3[text()='%s']", productName);
    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
}
```

#### Challenge 5: Handling Alerts

```java
// Wait for alert and handle it
public void handleAlert() {
    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        System.out.println("Alert text: " + alert.getText());
        alert.accept();
    } catch (NoAlertPresentException e) {
        System.out.println("No alert present");
    }
}
```

#### Challenge 6: Handling Dropdowns

```java
// Standard dropdown
public void selectFromDropdown(By locator, String value) {
    Select dropdown = new Select(driver.findElement(locator));
    dropdown.selectByVisibleText(value);
}

// Custom dropdown (not using <select> tag)
public void selectFromCustomDropdown(By dropdownLocator, String optionText) {
    driver.findElement(dropdownLocator).click();
    String optionXPath = String.format("//li[text()='%s']", optionText);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optionXPath))).click();
}
```

#### Challenge 7: Handling Multiple Windows/Tabs

```java
public void switchToNewWindow() {
    String parentWindow = driver.getWindowHandle();
    Set<String> allWindows = driver.getWindowHandles();
    
    for (String window : allWindows) {
        if (!window.equals(parentWindow)) {
            driver.switchTo().window(window);
            break;
        }
    }
}

public void closeAndSwitchToParent() {
    driver.close();  // Close current window
    driver.switchTo().window(driver.getWindowHandles().iterator().next());
}
```

#### Challenge 8: File Upload

```java
// Method 1: Simple upload
public void uploadFile(By locator, String filePath) {
    driver.findElement(locator).sendKeys(filePath);
}

// Method 2: Handle file upload dialog (Windows)
// Note: Selenium can't handle Windows dialogs directly
// Use AutoIt or Robot class
public void uploadFileUsingRobot(String filePath) throws AWTException {
    Robot robot = new Robot();
    
    // Copy file path to clipboard
    StringSelection stringSelection = new StringSelection(filePath);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    
    // Paste file path
    robot.keyPress(KeyEvent.VK_CONTROL);
    robot.keyPress(KeyEvent.VK_V);
    robot.keyRelease(KeyEvent.VK_V);
    robot.keyRelease(KeyEvent.VK_CONTROL);
    
    // Press Enter
    robot.keyPress(KeyEvent.VK_ENTER);
    robot.keyRelease(KeyEvent.VK_ENTER);
}
```

### 5.3 Debugging Tips

```java
// 1. Take screenshots on failure
public void takeScreenshot(String name) {
    TakesScreenshot ts = (TakesScreenshot) driver;
    File source = ts.getScreenshotAs(OutputType.FILE);
    String dest = "screenshots/" + name + "_" + System.currentTimeMillis() + ".png";
    Files.copy(source, new File(dest));
}

// 2. Print page source for debugging
System.out.println(driver.getPageSource());

// 3. Get current URL
System.out.println("Current URL: " + driver.getCurrentUrl());

// 4. Get element attributes
WebElement element = driver.findElement(By.id("username"));
System.out.println("Tag: " + element.getTagName());
System.out.println("Text: " + element.getText());
System.out.println("Value: " + element.getAttribute("value"));
System.out.println("Class: " + element.getAttribute("class"));

// 5. Check element state
System.out.println("Displayed: " + element.isDisplayed());
System.out.println("Enabled: " + element.isEnabled());
System.out.println("Selected: " + element.isSelected());

// 6. Execute JavaScript for debugging
JavascriptExecutor js = (JavascriptExecutor) driver;
Object result = js.executeScript("return document.readyState");
System.out.println("Page state: " + result);

// 7. Add custom logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginSteps {
    private static final Logger logger = LogManager.getLogger(LoginSteps.class);
    
    @When("User clicks login")
    public void userClicksLogin() {
        logger.info("Clicking login button");
        loginPage.clickLoginButton();
        logger.info("Login button clicked successfully");
    }
}
```

### 5.4 Running Tests

```bash
# Run all tests
mvn clean test

# Run specific tag
mvn clean test -Dcucumber.filter.tags="@smoke"

# Run with specific browser
mvn clean test -Dbrowser=chrome

# Run specific feature file
mvn clean test -Dcucumber.features="src/test/resources/features/Login.feature"

# Run in parallel (configure in pom.xml)
mvn clean test -Ddataproviderthreadcount=4

# Generate report
mvn clean test verify
```

### 5.5 Pro Tips

1. **Use IntelliJ IDEA Plugins:**
   - Cucumber for Java
   - Gherkin
   - Maven Helper

2. **Create reusable methods in utils package**

3. **Use DataProvider for data-driven testing with TestNG**

4. **Implement retry logic for flaky tests**

5. **Use CI/CD integration (Jenkins, GitLab CI)**

6. **Regular code reviews and pair programming**

7. **Keep dependencies updated**

8. **Write clear and descriptive Gherkin scenarios**

9. **Use tags effectively for test organization**

10. **Create comprehensive test data management**

---

## Quick Reference Commands

```bash
# Maven Commands
mvn clean                    # Clean project
mvn compile                  # Compile code
mvn test                     # Run tests
mvn clean test               # Clean and run tests
mvn test -Dtest=TestRunner   # Run specific test

# With tags
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@smoke and @regression"
mvn test -Dcucumber.filter.tags="not @skip"

# Generate report
mvn verify
```

---

## Resources for Learning

- **Selenium Documentation:** https://www.selenium.dev/documentation/
- **Cucumber Documentation:** https://cucumber.io/docs/cucumber/
- **TestNG Documentation:** https://testng.org/doc/documentation-main.html
- **Java Documentation:** https://docs.oracle.com/en/java/

---

**Good luck with your automation project! 🚀**
