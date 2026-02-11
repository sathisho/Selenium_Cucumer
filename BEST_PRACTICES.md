# Selenium Cucumber Framework - Best Practices Guide

## 🎯 Testing Best Practices

### 1. Test Naming Convention
```java
// ✅ Good
@Test
public void shouldSuccessfullyLoginWithValidCredentials()

@Test  
public void shouldDisplayErrorMessageForInvalidPassword()

// ❌ Bad
@Test
public void test1()

@Test
public void loginTest()
```

### 2. Use Page Object Model (POM)
```java
// ✅ Good - Encapsulates page elements and actions
public class LoginPage {
    private WebDriver driver;
    private By usernameField = By.id("user-name");
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }
}

// ❌ Bad - Direct element access in test
@Test
public void loginTest() {
    driver.findElement(By.id("user-name")).sendKeys("user");
}
```

### 3. Explicit Waits Over Implicit
```java
// ✅ Good - Explicit wait for specific condition
EnhancedWaits waits = new EnhancedWaits(driver, 10);
WebElement element = waits.waitForElementVisibility(By.id("element"));

// ⚠️ Avoid - Implicit waits apply to all find operations
driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
```

### 4. Separate Test Data from Test Logic
```java
// ✅ Good - Use external data sources
@DataProvider
public Object[][] loginCredentials() {
    return new Object[][] {
        {"standard_user", "secret_sauce", true},
        {"invalid_user", "wrong_pass", false}
    };
}

// ❌ Bad - Hardcoded test data
@Test
public void login() {
    login("standard_user", "secret_sauce");
}
```

### 5. Use Logging for Troubleshooting
```java
private static final Logger logger = LogManager.getLogger(LoginSteps.class);

@When("User enters username {string}")
public void userEntersUsername(String username) {
    logger.info("Entering username: {}", username);
    try {
        loginPage.enterUsername(username);
        logger.debug("Username entered successfully");
    } catch (Exception e) {
        logger.error("Failed to enter username", e);
        throw e;
    }
}
```

### 6. Clean Up Resources
```java
@After
public void tearDown(Scenario scenario) {
    // Capture screenshot on failure
    if (scenario.isFailed()) {
        ScreenshotUtil.takeScreenshot(scenario.getName());
    }
    
    // Close browser
    DriverManager.quitDriver();
    
    logger.info("Test cleanup completed");
}
```

---

## 🏗️ Code Organization Best Practices

### 1. Use Meaningful Variable Names
```java
// ✅ Good
boolean isLoginSuccessful = loginPage.verifyLoginSuccess();
String actualErrorMessage = loginPage.getErrorMessage();

// ❌ Bad
boolean b = loginPage.verify();
String msg = loginPage.getMsg();
```

### 2. Keep Methods Small and Focused
```java
// ✅ Good - Single responsibility
public void userSuccessfullyLogsIn() {
    userEntersValidCredentials();
    userClicksLoginButton();
    userIsRedirectedToHomePage();
}

// ❌ Bad - Too many responsibilities
public void loginTest() {
    driver.findElement(By.id("username")).sendKeys("user");
    driver.findElement(By.id("password")).sendKeys("pass");
    driver.findElement(By.id("login")).click();
    Thread.sleep(3000); // Never use hard waits!
    assertThat(driver.getCurrentUrl()).contains("/home");
}
```

### 3. Avoid Hard Waits (Thread.sleep)
```java
// ❌ Bad - Can fail randomly
Thread.sleep(5000);

// ✅ Good - Wait for specific condition
waits.waitForElementVisibility(By.id("element"));
```

### 4. Use Constants for Commonly Used Values
```java
// ✅ Good
public class TestConstants {
    public static final String BASE_URL = "https://www.saucedemo.com";
    public static final String VALID_USERNAME = "standard_user";
    public static final String VALID_PASSWORD = "secret_sauce";
    public static final int DEFAULT_TIMEOUT = 10;
}

// Usage
driver.navigate().to(TestConstants.BASE_URL);
```

### 5. Implement Proper Error Handling
```java
// ✅ Good
try {
    element.click();
} catch (ElementNotInteractableException e) {
    logger.warn("Element not interactable, using JavaScript click");
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
} catch (Exception e) {
    logger.error("Unexpected error during click operation", e);
    throw new AutomationException("Failed to click element", e);
}

// ❌ Bad
try {
    element.click();
} catch (Exception e) {
    // Silent failure - very bad!
}
```

---

## 📊 Test Data Best Practices

### 1. Use Scenario Outline for Multiple Test Cases
```gherkin
# ✅ Good - Reusable scenario with multiple data sets
Scenario Outline: Login with different user types
    Given User navigates to login page
    When User enters "<username>" and "<password>"
    Then User sees "<expectedResult>"
    
    Examples:
      | username           | password      | expectedResult    |
      | standard_user      | secret_sauce  | Products page     |
      | locked_out_user    | secret_sauce  | Locked out error  |
      | problem_user       | secret_sauce  | Products page     |

# ❌ Bad - Duplicate scenarios
Scenario: Login as standard user
    Given User navigates to login page
    When User enters "standard_user" and "secret_sauce"
    Then User sees "Products page"

Scenario: Login as locked user
    Given User navigates to login page
    When User enters "locked_out_user" and "secret_sauce"
    Then User sees "Locked out error"
```

### 2. Use External Test Data Files
```java
// ✅ Good - Load from Excel or CSV
List<User> users = TestDataReader.readUsersFromExcel("test-data/users.xlsx");

// ✅ Good - Load from JSON
User user = TestDataReader.readUserFromJson("test-data/user.json");

// ❌ Bad - Hardcoded data
String[] users = {"user1", "user2", "user3"};
```

### 3. Separate Positive and Negative Test Cases
```gherkin
Feature: Login Functionality

  # Positive scenarios
  @smoke @positive
  Scenario: Successful login with valid credentials
    
  # Negative scenarios
  @regression @negative
  Scenario: Login fails with invalid password
    
  @regression @negative
  Scenario: Login fails with invalid username
```

---

## 🔍 Debugging & Troubleshooting Best Practices

### 1. Use Detailed Logging
```java
logger.debug("Before clicking login button");
logger.debug("Current URL: {}", driver.getCurrentUrl());
logger.debug("Page title: {}", driver.getTitle());
loginButton.click();
logger.debug("After clicking login button");
```

### 2. Capture Screenshots on Failure
```java
@After
public void tearDown(Scenario scenario) {
    if (scenario.isFailed()) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAsFile();
        scenario.attach(screenshot, "image/png", scenario.getName());
    }
}
```

### 3. Use Soft Assertions for Multiple Validations
```java
// ✅ Good - All assertions run, report all failures
SoftAssertions softAssert = new SoftAssertions();
softAssert.assertThat(actualUsername).isEqualTo(expectedUsername);
softAssert.assertThat(actualEmail).isEqualTo(expectedEmail);
softAssert.assertThat(actualPhone).isEqualTo(expectedPhone);
softAssert.assertAll();

// ❌ Bad - Test stops at first failure
assertThat(actualUsername).isEqualTo(expectedUsername);
assertThat(actualEmail).isEqualTo(expectedEmail);
assertThat(actualPhone).isEqualTo(expectedPhone);
```

### 4. Use Breakpoints in IDE for Debugging
```java
// Set breakpoint on this line in IDE
EnhancedWaits waits = new EnhancedWaits(driver, 10); // Breakpoint here
waits.waitForElementVisibility(By.id("element"));
```

---

## 🚀 Performance Optimization

### 1. Use Parallel Execution
```xml
<!-- testng.xml -->
<suite name="Suite" parallel="methods" thread-count="5">
```

### 2. Run Tests in Headless Mode
```bash
mvn test -Dheadless=true
```

### 3. Disable Unnecessary Features
```properties
# config.properties
disable.notifications=true
disable.extensions=true
```

### 4. Use Lightweight Waits
```java
// ⚠️ 15 second wait might be too long
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

// ✅ Use contextual waits
EnhancedWaits waits = new EnhancedWaits(driver, 5); // For quick elements
EnhancedWaits longWaits = new EnhancedWaits(driver, 20); // For slow operations
```

### 5. Optimize Page Load Strategies
```java
// ✅ Use partial page load strategy
driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
```

---

## 📋 Test Documentation Best Practices

### 1. Write Descriptive Feature Files
```gherkin
# ✅ Good - Clear, descriptive features and scenarios
Feature: E-commerce Shopping Cart
  As a customer
  I want to add products to my cart
  So that I can purchase them later

  Background:
    Given User is logged in to the application
    And User is on the products page

  Scenario: Add single product to cart
    When User adds "Sauce Labs Backpack" to cart
    Then Product should be added to cart
    And Cart count should increase by 1

# ❌ Bad - Vague descriptions
Feature: Test
  Scenario: Test 1
    When User does something
    Then Something happens
```

### 2. Add Comments for Complex Logic
```java
// ✅ Good - Explain why, not what
// Retry click if element becomes stale during page transition
try {
    element.click();
} catch (StaleElementReferenceException e) {
    element = driver.findElement(locator);
    element.click();
}

// ❌ Bad - Obvious comments
// Click the element
element.click();
```

### 3. Use JavaDoc for Public Methods
```java
/**
 * Waits for an element to be visible on the page.
 * 
 * @param locator - By locator of the element to wait for
 * @return WebElement - The visible element
 * @throws TimeoutException - If element is not visible within timeout
 */
public WebElement waitForElementVisibility(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
}
```

---

## ✅ Pre-Commit Checklist

Before committing code:

- [ ] All tests pass locally
- [ ] Code follows naming conventions
- [ ] No hardcoded values (use constants)
- [ ] Logging is appropriate
- [ ] No Thread.sleep() used
- [ ] Exception handling is proper
- [ ] Screenshots/artifacts captured on failure
- [ ] Documentation is updated
- [ ] No sensitive data in code
- [ ] Code is reviewed by peer

---

## 🎓 Key Takeaways

1. **Keep It Simple** - Readable code is maintainable code
2. **Don't Repeat Yourself** - DRY principle applies to tests too
3. **Explicit Over Implicit** - Be clear about what you're testing
4. **Test Isolation** - Each test should be independent
5. **Meaningful Names** - Names should describe intent
6. **Proper Cleanup** - Always clean up after tests
7. **Use Logs** - Logs are your best debugging tool
8. **Optimize Wisely** - Performance matters, but correctness first
9. **Document Well** - Future you will thank present you
10. **Review & Refactor** - Continuously improve your code

---

**Last Updated:** February 2026
