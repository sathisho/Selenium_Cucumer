package runners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import utils.EmailUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TestNG Listener for sending email reports after test execution
 */
public class TestEmailListener extends TestListenerAdapter implements ITestListener {
    
    private static final Logger logger = LogManager.getLogger(TestEmailListener.class);
    private static int passedCount = 0;
    private static int failedCount = 0;
    private static int skippedCount = 0;
    
    @Override
    public void onTestSuccess(ITestResult result) {
        passedCount++;
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        failedCount++;
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        skippedCount++;
    }
    
    @Override
    public void onFinish(org.testng.ITestContext context) {
        int totalTests = passedCount + failedCount + skippedCount;
        
        logger.info("========== TEST EXECUTION SUMMARY ==========");
        logger.info("Total Tests: " + totalTests);
        logger.info("Passed: " + passedCount);
        logger.info("Failed: " + failedCount);
        logger.info("Skipped: " + skippedCount);
        logger.info("==========================================");
        
        // Determine overall status
        String testStatus = failedCount > 0 ? "FAILED" : "PASSED";
        
        // Send email with report
        String reportPath = "test-output/reports/ExtentReport.html";
        EmailUtil.sendReportEmail(testStatus, reportPath);
        
        // Reset counters for next run
        passedCount = 0;
        failedCount = 0;
        skippedCount = 0;
    }
}
