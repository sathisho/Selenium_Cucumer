package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to read configuration from config.properties file
 */
public class ConfigReader {
    
    private Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    
    public ConfigReader() {
        loadProperties();
    }
    
    private void loadProperties() {
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties = new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file from: " + CONFIG_FILE_PATH);
        }
    }
    
    public String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }
    
    public boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }
    
    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "10"));
    }
    
    public int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait", "20"));
    }
    
    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("page.load.timeout", "30"));
    }
    
    public String getBaseUrl() {
        return properties.getProperty("base.url");
    }
    
    public String getTestDataPath() {
        return properties.getProperty("test.data.path");
    }
    
    public String getScreenshotPath() {
        return properties.getProperty("screenshot.path", "test-output/screenshots/");
    }
    
    public String getReportPath() {
        return properties.getProperty("report.path", "test-output/reports/");
    }
    
    public int getMaxRetryCount() {
        return Integer.parseInt(properties.getProperty("max.retry.count", "2"));
    }
    
    public int getThreadCount() {
        return Integer.parseInt(properties.getProperty("thread.count", "3"));
    }
    
    public String getApiBaseUrl() {
        return properties.getProperty("api.base.url");
    }
    
    public int getApiTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout", "10"));
    }
    
    public String getDatabaseDriver() {
        return properties.getProperty("db.driver");
    }
    
    public String getDatabaseUrl() {
        return properties.getProperty("db.url");
    }
    
    public String getDatabaseUsername() {
        return properties.getProperty("db.username");
    }
    
    public String getDatabasePassword() {
        return properties.getProperty("db.password");
    }
    
    public int getDatabaseConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("db.connection.timeout", "10"));
    }
    
    public String getReportFormat() {
        return properties.getProperty("report.format", "html");
    }
    
    public boolean isTakeScreenshots() {
        return Boolean.parseBoolean(properties.getProperty("take.screenshots", "true"));
    }
    
    public boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(properties.getProperty("screenshot.on.failure", "true"));
    }
    
    public boolean isScreenshotOnEveryStep() {
        return Boolean.parseBoolean(properties.getProperty("screenshot.on.every.step", "false"));
    }
    
    public boolean isEnableHeadless() {
        return Boolean.parseBoolean(properties.getProperty("enable.headless", "false"));
    }
    
    public boolean isPerformanceLogging() {
        return Boolean.parseBoolean(properties.getProperty("performance.logging", "false"));
    }
    
    public boolean isDisableNotifications() {
        return Boolean.parseBoolean(properties.getProperty("disable.notifications", "true"));
    }
}
