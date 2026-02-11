package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Listeners;

/**
 * TestNG Cucumber Test Runner
 * 
 * This class is responsible for running all Cucumber feature files
 * with the specified configuration
 */
@Listeners({TestEmailListener.class})
@CucumberOptions(
        // Path to feature files
        features = "src/test/resources/features",
        
        // Path to step definitions and hooks
        glue = {"stepdefinitions", "hooks"},
        
        // Tags to run - you can customize this
        // Examples:
        // "@smoke" - run only smoke tests
        // "@smoke or @regression" - run smoke OR regression tests
        // "@smoke and @regression" - run tests with both tags
        // "not @skip" - run all except tests tagged with @skip
        tags = "@smoke",
        
        // Plugins for reporting
        plugin = {
                "pretty",                                           // Console output
                "html:test-output/cucumber-reports/cucumber.html", // HTML report
                "json:test-output/cucumber-reports/cucumber.json", // JSON report
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", // Extent report
                "rerun:test-output/failed_scenarios.txt"           // Failed scenarios file
        },
        
        // Make console output readable
        monochrome = true,
        
        // Check if all steps have corresponding step definitions without running tests
        dryRun = false,
        
        // Show detailed step execution
        publish = false
)
public class TestRunner extends AbstractTestNGCucumberTests {
    // This class will be empty
    // TestNG will automatically discover and run feature files based on @CucumberOptions
}
