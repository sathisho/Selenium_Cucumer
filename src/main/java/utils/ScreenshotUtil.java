package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for taking screenshots
 */
public class ScreenshotUtil {
    
    private static ConfigReader config = new ConfigReader();
    
    /**
     * Take screenshot and save to file
     * @param driver WebDriver instance
     * @param scenarioName Name of the scenario
     * @return Path to the screenshot file
     */
    public static String takeScreenshot(WebDriver driver, String scenarioName) {
        try {
            // Create screenshots directory if it doesn't exist
            File directory = new File(config.getScreenshotPath());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Generate timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            
            // Clean scenario name for filename
            String cleanScenarioName = scenarioName.replaceAll("[^a-zA-Z0-9]", "_");
            
            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            
            // Define destination
            String destination = config.getScreenshotPath() + cleanScenarioName + "_" + timestamp + ".png";
            File finalDestination = new File(destination);
            
            // Copy file to destination
            Files.copy(source.toPath(), finalDestination.toPath());
            
            System.out.println("Screenshot saved: " + destination);
            return destination;
            
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Take screenshot and return as byte array (for Cucumber reports)
     * @param driver WebDriver instance
     * @return Screenshot as byte array
     */
    public static byte[] takeScreenshotAsBytes(WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        return ts.getScreenshotAs(OutputType.BYTES);
    }
}
