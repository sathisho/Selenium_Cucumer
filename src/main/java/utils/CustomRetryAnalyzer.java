package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Custom Retry Analyzer for TestNG - Automatically retries failed tests
 */
public class CustomRetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = LogManager.getLogger(CustomRetryAnalyzer.class);
    private static final int MAX_RETRY_COUNT = 2; // Retry failed tests up to 2 times
    private int currentRetryCount = 0;
    
    @Override
    public boolean retry(ITestResult result) {
        if (currentRetryCount < MAX_RETRY_COUNT && result.getStatus() == ITestResult.FAILURE) {
            currentRetryCount++;
            logger.warn("Retrying test: {} | Retry count: {}/{}", 
                result.getMethod().getMethodName(), currentRetryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }
}
