# Selenium Cucumber BDD Framework - Complete Guide

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Setup & Installation](#setup--installation)
3. [Project Structure](#project-structure)
4. [Running Tests](#running-tests)
5. [Advanced Features](#advanced-features)
6. [Best Practices](#best-practices)
7. [Troubleshooting](#troubleshooting)
8. [CI/CD Integration](#cicd-integration)

---

## 📌 Project Overview

This is an enterprise-grade **Selenium Cucumber BDD Framework** built with:
- **Java 21 LTS** - Latest Java runtime
- **Selenium WebDriver 4.16.1** - Web automation
- **Cucumber 7.15.0** - BDD test specifications
- **TestNG 7.8.0** - Test execution & reporting
- **Allure Reports** - Advanced reporting with trends & history
- **REST Assured** - API testing capabilities
- **JaCoCo** - Code coverage analysis
- **Docker & CI/CD** - Containerization & automation

### Key Features ✨
- ✅ **Cross-Browser Testing** (Chrome, Firefox, Edge, Safari)
- ✅ **Parallel Test Execution** - Run tests concurrently
- ✅ **Automatic Test Retry** - Handle flaky tests
- ✅ **Enhanced Wait Strategies** - Explicit, fluent, custom waits
- ✅ **Database Integration** - Query validation
- ✅ **API Testing** - REST API automation
- ✅ **Email Reporting** - Automated email with reports
- ✅ **Headless Browser Mode** - Performance optimization
- ✅ **Multi-Format Reporting** - Allure, ExtentReports, HTML

---

## 🚀 Setup & Installation

### Prerequisites
```bash
# Required
- Java 21 LTS
- Maven 3.9+
- Git

# Optional
- Docker & Docker Compose
- Chrome/Firefox WebDriver
```

### Step 1: Clone Repository
```bash
git clone https://github.com/your-org/selenium-cucumber-framework.git
cd selenium-cucumber-framework
```

### Step 2: Install Dependencies
```bash
mvn clean install
```

### Step 3: Verify Installation
```bash
mvn --version
java -version
```

---

## 📁 Project Structure

```
selenium-cucumber-framework/
├── src/
│   ├── main/java/
│   │   ├── hooks/
│   │   │   └── Hooks.java              # Cucumber lifecycle hooks
│   │   ├── pages/
│   │   │   ├── LoginPage.java          # Page Object Model
│   │   │   └── ProductsPage.java       # Page Object Model
│   │   └── utils/
│   │       ├── DriverManager.java      # WebDriver lifecycle
│   │       ├── EnhancedWaits.java      # Advanced wait strategies
│   │       ├── BrowserFactory.java     # Cross-browser support
│   │       ├── APITestUtil.java        # REST API testing
│   │       ├── DatabaseUtil.java       # Database operations
│   │       ├── ScreenshotUtil.java     # Screenshot capture
│   │       ├── EmailUtil.java          # Email reporting
│   │       ├── SeleniumActions.java    # Common actions
│   │       ├── ConfigReader.java       # Config management
│   │       └── CustomRetryAnalyzer.java# Retry logic
│   │
│   └── test/java/
│       ├── runners/
│       │   ├── TestRunner.java         # Main test runner
│       │   ├── TestEmailListener.java  # Email listener
│       │   └── RetryAnalyzer.java      # Retry analyzer
│       ├── stepdefinitions/
│       │   ├── LoginSteps.java         # Login step implementations
│       │   └── ProductSteps.java       # Product step implementations
│       └── resources/
│           ├── config.properties       # Configuration
│           ├── email.properties        # Email setup
│           ├── extent-config.xml       # Report styling
│           ├── log4j2.xml              # Logging config
│           └── features/
│               ├── Login.feature       # Login BDD scenarios
│               └── Products.feature    # Product BDD scenarios
│
├── .github/workflows/
│   └── tests.yml                       # GitHub Actions CI/CD
├── pom.xml                             # Maven dependencies
├── testng.xml                          # TestNG configuration
├── Dockerfile                          # Docker containerization
├── docker-compose.yml                  # Docker Compose setup
└── README.md                           # This file
```

---

## 🧪 Running Tests

### Basic Test Execution
```bash
# Run all tests
mvn clean test

# Run with specific tag
mvn test -Dcucumber.filter.tags="@smoke"

# Run with specific tag and browser
mvn test -Dcucumber.filter.tags="@regression" -Dbrowser=firefox
```

### Advanced Test Execution

#### Run Tests in Parallel
```bash
# Run 5 tests in parallel
mvn test -DthreadCount=5
```

#### Run Tests in Headless Mode (Performance)
```bash
mvn test -Dheadless=true
```

#### Run Tests with Retry for Flaky Tests
```bash
# Automatically retry failed tests (max 2 times)
mvn test -Dmax.retry.count=2
```

#### Run Specific Test Class
```bash
mvn test -Dtest=TestRunner
```

#### Run with Code Coverage
```bash
mvn clean test jacoco:report
# Report at: target/site/jacoco/index.html
```

---

## 🎯 Advanced Features

### 1. Enhanced Wait Strategies
```java
EnhancedWaits waits = new EnhancedWaits(driver, 15);

// Explicit wait for element visibility
waits.waitForElementVisibility(By.id("element"));

// Wait for element to be clickable
waits.waitForElementToBeClickable(By.id("button"));

// Custom wait for JavaScript ready
waits.waitForJavaScriptReady();

// Wait for AJAX to complete
waits.waitForAjaxComplete();

// Wait for attribute value
waits.waitForAttributeValue(By.id("element"), "class", "active");
```

### 2. Cross-Browser Testing
```bash
# Run on Chrome
mvn test -Dbrowser=chrome

# Run on Firefox
mvn test -Dbrowser=firefox

# Run on Edge
mvn test -Dbrowser=edge

# Run on Safari (macOS only)
mvn test -Dbrowser=safari
```

### 3. Parallel Test Execution
In `testng.xml`:
```xml
<suite name="Suite" parallel="methods" thread-count="5">
```

Or update `config.properties`:
```properties
thread.count=5
```

### 4. Database Integration
```java
// Connect to database
DatabaseUtil.connect();

// Execute query and get results
List<Map<String, String>> results = DatabaseUtil.executeQuery("SELECT * FROM users");

// Verify data
for (Map<String, String> row : results) {
    System.out.println(row.get("username"));
}

// Disconnect
DatabaseUtil.disconnect();
```

### 5. API Testing with REST Assured
```java
APIUtil apiUtil = new APIUtil("https://api.example.com");

// GET Request
Response getResponse = apiUtil.get("/users/1");

// Verify status code
apiUtil.verifyStatusCode(getResponse, 200);

// Extract JSON path value
String userName = (String) apiUtil.extractJsonPath(getResponse, "name");

// POST Request
String requestBody = "{\"name\":\"John\",\"email\":\"john@example.com\"}";
Response postResponse = apiUtil.post("/users", requestBody);

// Set Authorization Header
apiUtil.setAuthorizationHeader("your-token-here");
```

### 6. Soft Assertions (Non-Blocking)
```java
import org.assertj.core.api.SoftAssertions;

SoftAssertions softAssert = new SoftAssertions();
softAssert.assertThat(actualText).contains("Expected");
softAssert.assertThat(actualValue).isEqualTo(expectedValue);
softAssert.assertThat(list).isNotEmpty();
softAssert.assertAll(); // Asserts all at end
```

### 7. Email Reporting
Set environment variable:
```bash
export GMAIL_APP_PASSWORD="your-16-char-app-password"
```

Tests will automatically send reports via email after execution.

### 8. Headless Mode Execution
```bash
# Run in headless mode (faster, less memory)
mvn test -Dheadless=true
```

### 9. Performance Optimization
```properties
# In config.properties
enable.headless=true
disable.notifications=true
performance.logging=true
```

---

## 📚 Best Practices

### 1. Page Object Model (POM)
```java
public class LoginPage {
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    
    private WebDriver driver;
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }
    
    public void clickLogin() {
        driver.findElement(loginButton).click();
    }
}
```

### 2. BDD Feature Files
```gherkin
Feature: User Login
  
  Scenario: Successful login with valid credentials
    Given User navigates to the login page
    When User enters username "standard_user"
    And User enters password "secret_sauce"
    And User clicks on login button
    Then User should be redirected to products page
```

### 3. Meaningful Test Names
```bash
# ❌ Bad
@Test
public void test1() {}

# ✅ Good
@Test
public void userShouldBeLoggedInWithValidCredentials() {}
```

### 4. Data-Driven Testing
Use Scenario Outline in Cucumber:
```gherkin
Scenario Outline: Login with different users
    Given User navigates to login page
    When User enters "<username>" and "<password>"
    Then User sees "<result>"
    
    Examples:
      | username      | password     | result           |
      | standard_user | secret_sauce | Products page    |
      | locked_user   | secret_sauce | Locked out error |
```

### 5. Explicit Waits Over Implicit
```java
// ❌ Avoid
driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

// ✅ Use
EnhancedWaits waits = new EnhancedWaits(driver, 10);
waits.waitForElementVisibility(By.id("element"));
```

### 6. Logging
```java
private static final Logger logger = LogManager.getLogger(MyClass.class);

logger.info("Test started");
logger.warn("Warning message");
logger.error("Error occurred", exception);
```

### 7. Proper Cleanup
```java
@After
public void tearDown() {
    if (driver != null) {
        driver.quit();
    }
}
```

---

## 🐛 Troubleshooting

### Issue: "WebDriver executable path not found"
```bash
# Solution: WebDriverManager will download automatically
# If manual: Download from https://chromedriver.chromium.org/
```

### Issue: "Test timeout or hang"
```bash
# Increase timeouts in config.properties
page.load.timeout=60
explicit.wait=30
```

### Issue: "Element not clickable"
```java
// Use JavaScript click
JavascriptExecutor js = (JavascriptExecutor) driver;
js.executeScript("arguments[0].click();", element);
```

### Issue: "Email not sending"
```bash
# Verify environment variable
echo $GMAIL_APP_PASSWORD

# Ensure 2FA enabled on Gmail account
# Generate App Password from: https://myaccount.google.com/apppasswords
```

### Issue: "Tests fail in headless mode"
```bash
# Some elements may not work in headless
# Test locally first, then enable headless
mvn test -Dheadless=false
```

---

## 🐳 Docker & CI/CD Integration

### Local Docker Execution
```bash
# Build Docker image
docker build -t selenium-bdd:latest .

# Run tests in Docker container
docker run --rm selenium-bdd:latest
```

### Docker Compose
```bash
# Start services (Selenium Grid, database, etc.)
docker-compose up -d

# Run tests against services
mvn test

# Stop services
docker-compose down
```

### GitHub Actions CI/CD
Tests automatically run on:
- Push to `main` or `develop` branches
- Pull requests
- Daily schedule (2 AM UTC)

View workflow: `.github/workflows/tests.yml`

### Generate Allure Report
```bash
# Generate and open report
mvn allure:report
open target/site/allure-maven-plugin/index.html
```

---

## 📊 Reporting

### Available Reports
1. **Allure Reports** - Advanced with trends
   - Path: `target/site/allure-maven-plugin/index.html`
   
2. **ExtentReports** - Visual with screenshots
   - Path: `test-output/reports/ExtentReport.html`
   
3. **TestNG Reports** - Default reports
   - Path: `target/surefire-reports/index.html`
   
4. **JaCoCo Code Coverage**
   - Path: `target/site/jacoco/index.html`

### Email Report
- Configured in `email.properties`
- Sent automatically after test execution
- Set `GMAIL_APP_PASSWORD` environment variable

---

## 📞 Support & Contributing

For issues, questions, or contributions:
1. Check existing documentation
2. Open GitHub issue with details
3. Submit pull request with improvements

---

## 📄 License

This project is licensed under MIT License - see LICENSE file for details.

---

**Last Updated:** February 2026
**Java Version:** 21 LTS
**Selenium Version:** 4.16.1
**Framework Status:** ✅ Production Ready
