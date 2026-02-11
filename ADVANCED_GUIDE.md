# Selenium Cucumber BDD Framework - Enhanced

A comprehensive automated testing framework built with Selenium WebDriver, Cucumber BDD, and TestNG. This framework includes multiple enhancements for modern test automation practices.

## 📋 Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Advanced Features](#advanced-features)
- [Reporting](#reporting)
- [CI/CD Integration](#cicd-integration)
- [Docker & Containerization](#docker--containerization)
- [Best Practices](#best-practices)
- [Troubleshooting](#troubleshooting)

## ✨ Features

### Core Features
- ✅ **Selenium WebDriver 4.16.1** - Latest browser automation
- ✅ **Cucumber 7.15.0** - BDD-style test definitions
- ✅ **TestNG 7.8.0** - Test execution and management
- ✅ **Page Object Model (POM)** - Maintainable page abstractions
- ✅ **Parallel Test Execution** - Run multiple tests simultaneously
- ✅ **Cross-Browser Testing** - Chrome, Firefox, Safari, Edge support
- ✅ **Headless Mode** - Fast CI/CD execution

### Advanced Features
- 🚀 **Allure Reports** - Beautiful test reports with trends and history
- ⏳ **Enhanced Wait Strategies** - Explicit waits, fluent waits, custom conditions
- 🔄 **Automatic Retry Mechanism** - Handle flaky tests gracefully
- 📊 **Database Integration** - Direct database validation
- 🌐 **REST API Testing** - API testing with REST Assured
- 📧 **Email Reporting** - Automated email delivery of test reports
- 🐳 **Docker Support** - Containerized test execution
- 📈 **Code Coverage** - JaCoCo for test coverage analysis
- 🔐 **Secure Credentials** - Environment variable support

## 📦 Prerequisites

- **Java**: JDK 21 or higher
- **Maven**: 3.6.0 or higher
- **Browsers**: Chrome, Firefox (for headless mode optional)
- **Git**: For version control
- **Docker** (Optional): For containerized execution

## 🗂️ Project Structure

```
selenium-cucumber-framework/
├── src/
│   ├── main/java/
│   │   ├── hooks/           # Cucumber lifecycle hooks
│   │   ├── pages/           # Page Object Models
│   │   ├── utils/           # Utility classes
│   │   │   ├── DriverManager.java       # WebDriver management
│   │   │   ├── SeleniumWaits.java       # Wait strategies
│   │   │   ├── BrowserFactory.java      # Multi-browser support
│   │   │   ├── APIUtil.java             # REST API testing
│   │   │   ├── DatabaseUtil.java        # Database operations
│   │   │   ├── ConfigReader.java        # Configuration management
│   │   │   ├── EmailUtil.java           # Email functionality
│   │   │   └── ScreenshotUtil.java      # Screenshot capture
│   │   └── [other utility classes]
│   └── test/
│       ├── java/
│       │   ├── stepdefinitions/         # Cucumber step implementations
│       │   ├── runners/                 # Test runners
│       │   │   ├── TestRunner.java      # Main test runner
│       │   │   ├── RetryAnalyzer.java   # Retry mechanism
│       │   │   └── TestEmailListener.java
│       │   └── tests/                   # API tests
│       └── resources/
│           ├── features/                # .feature files (BDD)
│           ├── config.properties        # Configuration
│           ├── email.properties         # Email config
│           ├── extent-config.xml        # Report styling
│           └── log4j2.xml               # Logging config
├── .github/workflows/
│   └── tests.yml                        # GitHub Actions CI/CD
├── Dockerfile                           # Docker image
├── docker-compose.yml                   # Docker containers
├── pom.xml                              # Maven dependencies
├── testng.xml                           # TestNG configuration
└── README.md                            # This file
```

## 🚀 Installation & Setup

### 1. Clone Repository
```bash
git clone https://github.com/yourusername/selenium-cucumber-framework.git
cd selenium-cucumber-framework
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Verify Installation
```bash
java -version    # Should be 21+
mvn -version     # Should be 3.6.0+
```

## ⚙️ Configuration

### Environment Variables
```bash
# Browser selection
export BROWSER=chrome              # chrome, firefox, safari, edge

# Headless mode
export HEADLESS=true              # true or false

# Email reporting
export GMAIL_APP_PASSWORD=xxxxxxxxxxxx  # 16-character Gmail App Password

# Database (if applicable)
export DB_URL=jdbc:mysql://localhost:3306/testdb
export DB_USERNAME=root
export DB_PASSWORD=password

# API Testing
export API_BASEURL=https://api.example.com
```

### Configuration Files

#### config.properties
```properties
baseUrl=https://www.saucedemo.com
browser=chrome
headless=false
implicitWait=10
pageLoadTimeout=30
```

#### email.properties
```properties
email.enabled=true
smtp.host=smtp.gmail.com
smtp.port=587
smtp.username=your-email@gmail.com
# Password from GMAIL_APP_PASSWORD env var
send.on.success=true
send.on.failure=true
recipient.emails=test@example.com
```

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Tag
```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@regression"
mvn test -Dcucumber.filter.tags="@smoke and @dataDriven"
```

### Run Parallel Tests
```bash
mvn test -DparallelMethod=3 -DparallelClass=2
```

### Run with Different Browser
```bash
mvn test -Dbrowser=firefox -Dheadless=false
mvn test -Dbrowser=chrome -Dheadless=true
mvn test -Dbrowser=edge
```

### Run with Retry
```bash
mvn test -Dretry=true  # Automatically retries failed tests
```

## 🎯 Advanced Features

### Enhanced Wait Strategies
```java
SeleniumWaits waits = new SeleniumWaits(driver);

// Explicit waits
waits.waitForElementVisible(By.id("element"));
waits.waitForElementClickable(By.xpath("//button"));
waits.waitForElementText(By.css(".message"), "Success");

// Fluent waits
waits.fluentWait(By.id("dynamic"), 15, 500);

// Custom conditions
waits.waitForJavaScriptToLoad();
waits.waitForAjaxToComplete();
waits.waitForJavaScriptCondition("return jQuery.active == 0");
```

### Automatic Retry for Flaky Tests
Add `@Retry` to tests that might be flaky:
```java
@Test
@Retry(analyzer = RetryAnalyzer.class)
public void testFlaky() {
    // Test code
}
```

### Cross-Browser Testing
```java
// Create WebDriver for specific browser
BrowserFactory.BrowserType browser = BrowserFactory.getBrowserType();
WebDriver driver = BrowserFactory.createDriver(browser, isHeadless);

// Or directly
WebDriver chromeDriver = BrowserFactory.createDriver(BrowserFactory.BrowserType.CHROME, false);
WebDriver firefoxHeadless = BrowserFactory.createDriver(BrowserFactory.BrowserType.FIREFOX, true);
```

### API Testing with REST Assured
```java
// Create new file: src/test/java/tests/APITests.java
Response response = APIUtil.get("/api/users/1");
Assert.assertTrue(APIUtil.validateStatusCode(response, 200));

String username = APIUtil.extractFromJSON(response, "username");
Assert.assertTrue(APIUtil.validateResponseContains(response, "John"));
```

### Database Integration
```java
// Connect to database
DatabaseUtil.connect();

// Execute query
List<Map<String, String>> results = DatabaseUtil.executeQuery("SELECT * FROM users");
String username = DatabaseUtil.getSingleValue("SELECT username FROM users WHERE id=1", "username");

// Check if record exists
boolean exists = DatabaseUtil.recordExists("SELECT * FROM users WHERE email='test@example.com'");

// Execute update
DatabaseUtil.executeUpdate("UPDATE users SET status='active' WHERE id=1");

// Disconnect
DatabaseUtil.disconnect();
```

## 📊 Reporting

### Allure Reports
```bash
# Generate Allure report
mvn allure:report

# Serve Allure report
mvn allure:serve
```

Report location: `target/site/allure-maven-plugin/`

### ExtentReports
Automatically generated at: `test-output/reports/ExtentReport.html`

### Code Coverage (JaCoCo)
```bash
mvn jacoco:report
```

Coverage report: `target/site/jacoco/index.html`

## 🔄 CI/CD Integration

### GitHub Actions
Tests automatically run on:
- Push to `main` or `develop` branches
- Pull requests
- Daily schedule (2 AM UTC)

Workflow file: `.github/workflows/tests.yml`

### SonarQube Integration
```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=selenium-cucumber-framework \
  -Dsonar.host.url=https://sonarqube.example.com \
  -Dsonar.login=YOUR_TOKEN
```

## 🐳 Docker & Containerization

### Build Docker Image
```bash
docker build -t selenium-framework:latest .
```

### Run with Docker
```bash
docker run -v $(pwd)/test-output:/app/test-output selenium-framework:latest
```

### Run with Docker Compose (Multi-container)
```bash
# Start all services (Selenium Grid, MySQL, Test Runner)
docker-compose up -d

# Run tests
docker-compose run test-runner

# View Allure reports
docker-compose up allure
# Access at http://localhost:4040
```

## 📚 Best Practices

### 1. Wait Strategies
- ❌ Avoid `Thread.sleep()` - use explicit waits instead
- ✅ Use `SeleniumWaits` for element interactions
- ✅ Set appropriate timeouts based on application

### 2. Page Object Model
```java
// ✅ Good: Encapsulated page methods
public class LoginPage {
    private By usernameField = By.id("username");
    
    public void enterUsername(String username) {
        SeleniumActions.type(driver, usernameField, username);
    }
}

// ❌ Avoid: Exposing locators
public By getLoginButton() {
    return By.id("login");  // Not recommended
}
```

### 3. Assertions
```java
// ✅ Use AssertJ for better readability
assertThat(result).isNotNull().contains("Success");

// Or use soft assertions for multiple validations
SoftAssert softAssert = new SoftAssert();
softAssert.assertTrue(condition1);
softAssert.assertTrue(condition2);
softAssert.assertAll();  // Reports all failures at once
```

### 4. Logging
```java
logger.info("User logged in successfully");
logger.error("Login failed: {}", errorMessage, exception);
```

### 5. Configuration Management
```java
// ✅ Use ConfigReader for all configurations
String baseUrl = ConfigReader.getProperty("baseUrl");

// ❌ Avoid hardcoding values
String baseUrl = "https://www.saucedemo.com";
```

### 6. Test Data
```java
// Use TestContainers for database fixtures
// Use JavaFaker for generating test data
String randomEmail = new Faker().internet().emailAddress();
```

## 🐛 Troubleshooting

### Common Issues

#### WebDriver Issues
```
Problem: "The path to the driver executable must be set"
Solution: WebDriverManager automatically handles this. Ensure WebDriverManager dependency is in pom.xml
```

#### Element Not Found
```
Problem: NoSuchElementException
Solution: Use SeleniumWaits instead of direct findElement()
// ❌ Don't use:
driver.findElement(By.id("element")).click();

// ✅ Use:
SeleniumWaits waits = new SeleniumWaits(driver);
waits.waitForElementClickable(By.id("element")).click();
```

#### Parallel Execution Issues
```
Problem: NoSuchMethodException in parallel tests
Solution: Ensure WebDriver is thread-safe using ThreadLocal in DriverManager
```

#### Email Not Sending
```
Problem: AuthenticationFailedException
Solution: 
1. Generate Gmail App Password (not regular password)
2. Enable 2FA on Gmail account
3. Set GMAIL_APP_PASSWORD environment variable
export GMAIL_APP_PASSWORD="your-16-char-app-password"
```

#### Screenshot Capture Fails
```
Problem: Screenshot directory doesn't exist
Solution: Ensure logs/ and test-output/screenshots/ directories exist
mkdir -p logs test-output/screenshots
```

## 📖 Additional Resources

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Cucumber BDD Guide](https://cucumber.io/docs/cucumber/)
- [TestNG Documentation](https://testng.org/doc/)
- [Allure Reports](https://docs.qameta.io/allure/)
- [REST Assured](https://rest-assured.io/)
- [Docker Documentation](https://docs.docker.com/)

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/new-feature`
2. Commit changes: `git commit -am 'Add new feature'`
3. Push to branch: `git push origin feature/new-feature`
4. Open Pull Request

## 📝 License

This project is licensed under the MIT License - see LICENSE file for details.

## 👥 Support

For issues and questions:
- Create an issue in GitHub
- Contact: sathish.oruganti45@gmail.com

---

**Last Updated**: February 2026  
**Java Version**: 21 LTS  
**Selenium Version**: 4.16.1  
**Framework Version**: 2.0
