# Selenium Cucumber BDD Automation Framework

A complete, production-ready Selenium automation framework using Java, Cucumber BDD, and TestNG.

## 📋 Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Running Tests](#running-tests)
- [Configuration](#configuration)
- [Writing Tests](#writing-tests)
- [Reports](#reports)
- [Best Practices](#best-practices)

## ✨ Features

- **Page Object Model (POM)** design pattern
- **Cucumber BDD** for behavior-driven testing
- **TestNG** for test execution and reporting
- **WebDriverManager** for automatic driver management
- **ExtentReports** for beautiful HTML reports
- **Screenshot capture** on test failures
- **Configurable** browser and environment settings
- **Parallel execution** support
- **Comprehensive logging** with Log4j2
- **Tag-based** test execution

## 🔧 Prerequisites

Before you begin, ensure you have the following installed:

- **Java JDK 11 or higher**
  - Download from: https://www.oracle.com/java/technologies/downloads/
  - Verify installation: `java -version`

- **Maven 3.6 or higher**
  - Download from: https://maven.apache.org/download.cgi
  - Verify installation: `mvn -version`

- **IDE** (Choose one)
  - IntelliJ IDEA (Recommended)
  - Eclipse
  - VS Code with Java extensions

- **Git** (Optional, for version control)

## 📁 Project Structure

```
selenium-cucumber-framework/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── hooks/
│   │       │   └── Hooks.java              # Cucumber hooks (Before/After)
│   │       ├── pages/
│   │       │   ├── LoginPage.java          # Login page objects
│   │       │   └── ProductsPage.java       # Products page objects
│   │       └── utils/
│   │           ├── ConfigReader.java       # Configuration reader
│   │           ├── DriverManager.java      # WebDriver management
│   │           ├── ScreenshotUtil.java     # Screenshot utilities
│   │           └── SeleniumActions.java    # Common Selenium actions
│   │
│   └── test/
│       ├── java/
│       │   ├── runners/
│       │   │   └── TestRunner.java         # TestNG Cucumber runner
│       │   └── stepdefinitions/
│       │       ├── LoginSteps.java         # Login step definitions
│       │       └── ProductSteps.java       # Products step definitions
│       │
│       └── resources/
│           ├── features/
│           │   ├── Login.feature           # Login scenarios
│           │   └── Products.feature        # Products scenarios
│           ├── config.properties           # Test configuration
│           ├── extent.properties           # Report configuration
│           ├── extent-config.xml           # Report styling
│           └── log4j2.xml                  # Logging configuration
│
├── pom.xml                                 # Maven dependencies
├── testng.xml                              # TestNG suite configuration
└── README.md                               # This file
```

## 🚀 Setup Instructions

### 1. Clone or Download the Project

```bash
# If using Git
git clone <repository-url>
cd selenium-cucumber-framework

# Or simply extract the zip file
```

### 2. Import into IDE

**IntelliJ IDEA:**
1. Open IntelliJ IDEA
2. File → Open → Select project folder
3. Wait for Maven to download dependencies (check bottom-right progress bar)

**Eclipse:**
1. Open Eclipse
2. File → Import → Maven → Existing Maven Projects
3. Browse to project folder → Finish

### 3. Install Dependencies

```bash
mvn clean install
```

This will download all required dependencies (Selenium, Cucumber, TestNG, etc.)

### 4. Verify Installation

```bash
mvn clean test
```

If setup is correct, tests should start running.

## ▶️ Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run with Specific Tags

```bash
# Run only smoke tests
mvn clean test -Dcucumber.filter.tags="@smoke"

# Run smoke OR regression tests
mvn clean test -Dcucumber.filter.tags="@smoke or @regression"

# Run smoke AND regression (tests must have both tags)
mvn clean test -Dcucumber.filter.tags="@smoke and @regression"

# Run all except skipped tests
mvn clean test -Dcucumber.filter.tags="not @skip"
```

### Run Specific Feature File

```bash
mvn clean test -Dcucumber.features="src/test/resources/features/Login.feature"
```

### Run with Specific Browser

Modify `src/test/resources/config.properties`:
```properties
browser=chrome    # chrome, firefox, or edge
```

### Run in Headless Mode

Modify `src/test/resources/config.properties`:
```properties
headless=true
```

### Run from IDE

**IntelliJ IDEA / Eclipse:**
- Right-click on `TestRunner.java` → Run
- Right-click on any `.feature` file → Run

## ⚙️ Configuration

### config.properties

Located at: `src/test/resources/config.properties`

```properties
# Browser settings
browser=chrome              # chrome, firefox, edge
headless=false             # true for headless mode

# Timeouts (in seconds)
implicit.wait=10
explicit.wait=20
page.load.timeout=30

# Application URL
base.url=https://www.saucedemo.com

# Paths
screenshot.path=test-output/screenshots/
report.path=test-output/reports/
```

### Valid Test Users (SauceDemo)

```
Username: standard_user        | Password: secret_sauce
Username: locked_out_user      | Password: secret_sauce
Username: problem_user         | Password: secret_sauce
Username: performance_glitch_user | Password: secret_sauce
```

## ✍️ Writing Tests

### 1. Create Feature File

Create a new `.feature` file in `src/test/resources/features/`

```gherkin
Feature: Shopping Cart
  
  @smoke
  Scenario: Add product to cart
    Given User is logged in
    When User adds "Sauce Labs Backpack" to cart
    Then Cart should show 1 item
```

### 2. Create Step Definitions

Create a new step definition class in `src/test/java/stepdefinitions/`

```java
package stepdefinitions;

import io.cucumber.java.en.*;

public class CartSteps {
    
    @When("User adds {string} to cart")
    public void user_adds_to_cart(String product) {
        // Your code here
    }
    
    @Then("Cart should show {int} item")
    public void cart_should_show_item(int count) {
        // Your code here
    }
}
```

### 3. Create Page Object

Create a new page class in `src/main/java/pages/`

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SeleniumActions;

public class CartPage {
    private WebDriver driver;
    private SeleniumActions actions;
    
    private By cartBadge = By.className("shopping_cart_badge");
    
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new SeleniumActions(driver);
    }
    
    public String getCartCount() {
        return actions.getText(cartBadge);
    }
}
```

## 📊 Reports

### ExtentReports

After test execution, open:
```
test-output/reports/ExtentReport.html
```

Features:
- Beautiful HTML dashboard
- Test statistics and charts
- Screenshots for failed tests
- Detailed step-by-step execution logs
- Environment information

### Cucumber HTML Report

```
test-output/cucumber-reports/cucumber.html
```

### Console Output

Test results are also printed to console with detailed logs.

### Screenshots

Failed test screenshots are saved to:
```
test-output/screenshots/
```

## 📝 Best Practices

### 1. Feature Files
- Write in business language, not technical terms
- Keep scenarios independent
- Use Background for common steps
- Use tags for organization (@smoke, @regression, @wip)

### 2. Step Definitions
- Keep steps reusable
- Don't put assertions in step definitions when possible
- Use descriptive names
- One step = one action

### 3. Page Objects
- One page = one class
- Keep locators at top of class
- Create methods for each action
- Return page objects for method chaining

### 4. Waits
- Always use explicit waits, not Thread.sleep()
- Use `SeleniumActions` utility methods
- Wait for specific conditions

### 5. Assertions
- Use meaningful assertion messages
- Assert on actual behavior, not implementation
- Keep assertions in step definitions

## 🐛 Troubleshooting

### Issue: Tests fail to start

**Solution:** 
- Verify Java and Maven are installed: `java -version`, `mvn -version`
- Run `mvn clean install` to download dependencies

### Issue: Browser doesn't open

**Solution:**
- Check `config.properties` browser setting
- WebDriverManager should auto-download drivers
- Try running with `headless=false`

### Issue: Element not found errors

**Solution:**
- Increase wait times in `config.properties`
- Check if element locators are correct
- Verify page is fully loaded before interaction

### Issue: Tests pass locally but fail in CI/CD

**Solution:**
- Use headless mode: `headless=true`
- Increase timeouts for slower CI environments
- Check for environment-specific issues

## 🎯 Tags Reference

- `@smoke` - Critical tests that must pass
- `@regression` - Full regression suite
- `@e2e` - End-to-end user journeys
- `@dataDriven` - Data-driven tests with examples
- `@wip` - Work in progress (usually skipped)
- `@skip` - Tests to skip

## 📚 Additional Resources

- **Selenium Documentation:** https://www.selenium.dev/documentation/
- **Cucumber Documentation:** https://cucumber.io/docs/cucumber/
- **TestNG Documentation:** https://testng.org/doc/
- **ExtentReports:** https://www.extentreports.com/

## 🤝 Contributing

1. Create a new branch for features
2. Follow existing code style
3. Write tests for new features
4. Update documentation

## 📄 License

This project is open source and available under the MIT License.

---

## 💡 Quick Start Commands

```bash
# Install dependencies
mvn clean install

# Run all tests
mvn clean test

# Run smoke tests only
mvn clean test -Dcucumber.filter.tags="@smoke"

# Run in headless mode (modify config.properties first)
mvn clean test

# Generate reports (reports auto-generated after test run)
# Open: test-output/reports/ExtentReport.html
```

---

**Happy Testing! 🚀**

For questions or issues, please create an issue in the project repository.
