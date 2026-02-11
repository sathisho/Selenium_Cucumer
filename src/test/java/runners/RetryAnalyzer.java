package runners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Custom TestNG Retry Analyzer for automatically retrying failed tests.
 * Helps handle flaky tests by retrying them a specified number of times.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2; // Number of retries

    /**
     * Determines if a test should be retried based on the retry count and max attempts
     */
    @Override
    public boolean retry(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            if (retryCount < MAX_RETRY_COUNT) {
                retryCount++;
                logger.warn("Test FAILED: {} - Retrying attempt {} of {}", 
                    result.getMethod().getMethodName(), retryCount, MAX_RETRY_COUNT);
                return true;
            } else {
                logger.error("Test FAILED: {} - All retry attempts exhausted", 
                    result.getMethod().getMethodName());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test PASSED: {} on retry attempt {}", 
                result.getMethod().getMethodName(), retryCount);
        }
        return false;
    }

    /**
     * Get current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * Reset retry count (call before each test)
     */
    public void resetRetryCount() {
        retryCount = 0;
    }
}
