package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Browser Factory for creating WebDriver instances with different browsers.
 * Supports Chrome, Firefox, Safari, and Edge browsers with optional headless mode.
 */
public class BrowserFactory {
    private static final Logger logger = LogManager.getLogger(BrowserFactory.class);

    /**
     * Enum for supported browsers
     */
    public enum BrowserType {
        CHROME("chrome"),
        FIREFOX("firefox"),
        SAFARI("safari"),
        EDGE("edge");

        private final String value;

        BrowserType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static BrowserType fromString(String value) {
            for (BrowserType type : BrowserType.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Browser type not found: " + value);
        }
    }

    /**
     * Create WebDriver based on browser type
     */
    public static WebDriver createDriver(BrowserType browserType) {
        return createDriver(browserType, false);
    }

    /**
     * Create WebDriver based on browser type with optional headless mode
     */
    public static WebDriver createDriver(BrowserType browserType, boolean headless) {
        WebDriver driver;
        logger.info("Creating WebDriver for browser: {}, Headless: {}", browserType.getValue(), headless);

        switch (browserType) {
            case CHROME:
                driver = createChromeDriver(headless);
                break;
            case FIREFOX:
                driver = createFirefoxDriver(headless);
                break;
            case SAFARI:
                driver = createSafariDriver();
                break;
            case EDGE:
                driver = createEdgeDriver(headless);
                break;
            default:
                throw new IllegalArgumentException("Browser type not supported: " + browserType);
        }

        configureCommonOptions(driver);
        logger.info("WebDriver created successfully for: {}", browserType.getValue());
        return driver;
    }

    /**
     * Create Chrome WebDriver
     */
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
            logger.info("Chrome running in headless mode");
        }

        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // Performance optimization
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-popup-blocking");

        return new ChromeDriver(options);
    }

    /**
     * Create Firefox WebDriver
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
            logger.info("Firefox running in headless mode");
        }

        options.addArguments("--start-maximized");

        // Performance optimization
        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);

        return new FirefoxDriver(options);
    }

    /**
     * Create Safari WebDriver
     */
    private static WebDriver createSafariDriver() {
        logger.info("Safari running (headless not supported for Safari)");
        return new SafariDriver();
    }

    /**
     * Create Edge WebDriver
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments("--headless=new");
            logger.info("Edge running in headless mode");
        }

        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Performance optimization
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");

        return new EdgeDriver(options);
    }

    /**
     * Configure common WebDriver options
     */
    private static void configureCommonOptions(WebDriver driver) {
        // Set implicit wait (though explicit waits are preferred)
        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(10));

        // Window management
        driver.manage().window().maximize();

        logger.debug("Common WebDriver options configured");
    }

    /**
     * Get browser type from system property or configuration
     */
    public static BrowserType getBrowserType() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        return BrowserType.fromString(browser);
    }

    /**
     * Get headless mode from system property
     */
    public static boolean isHeadlessMode() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"));
    }
}
