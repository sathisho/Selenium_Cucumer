package hooks;

import io.cucumber.java.*;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;
import utils.ScreenshotUtil;

/**
 * Cucumber Hooks for setup and teardown operations
 */
public class Hooks {
    
    private WebDriver driver;
    
    @Before
    public void setUp(Scenario scenario) {
        System.out.println("========================================");
        System.out.println("Starting Scenario: " + scenario.getName());
        System.out.println("========================================");
        driver = DriverManager.getDriver();
    }
    
    @After
    public void tearDown(Scenario scenario) {
        // Take screenshot if scenario fails
        if (scenario.isFailed()) {
            System.out.println("Scenario FAILED: " + scenario.getName());
            byte[] screenshot = ScreenshotUtil.takeScreenshotAsBytes(driver);
            scenario.attach(screenshot, "image/png", scenario.getName());
            
            // Also save to file
            ScreenshotUtil.takeScreenshot(driver, scenario.getName());
        } else {
            System.out.println("Scenario PASSED: " + scenario.getName());
        }
        
        System.out.println("Status: " + scenario.getStatus());
        System.out.println("========================================\n");
        
        // Quit driver
        DriverManager.quitDriver();
    }
    
    @BeforeStep
    public void beforeStep() {
        // Code to execute before each step (optional)
    }
    
    @AfterStep
    public void afterStep(Scenario scenario) {
        // Take screenshot after each step
        try {
            byte[] screenshot = ScreenshotUtil.takeScreenshotAsBytes(driver);
            scenario.attach(screenshot, "image/png", "Step - " + scenario.getName());
        } catch (Exception e) {
            System.out.println("Could not capture screenshot: " + e.getMessage());
        }
    }
}
