package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverManager {

    public static Logger logger = Logger.getLogger(DriverManager.class.getName());
    private static ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver(String testBrowser) {
        WebDriver driver = null;
        switch (testBrowser.toLowerCase()) {
            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("start-maximized");
                firefoxOptions.addArguments("--remote-allow-origins=*");
                driver = new FirefoxDriver(firefoxOptions);
                logger.info("***** Browser is firefox *****");
                break;
            }
            case "safari": {
                WebDriverManager.safaridriver().setup();
                SafariOptions safariOptions = new SafariOptions();
                driver = new SafariDriver(safariOptions);
                logger.info("***** Browser is Safari *****");
                break;
            }
            case "chrome": {
                WebDriverManager.chromedriver().browserVersion("118.0.5993.71").setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("start-maximized");
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
                logger.info("***** Browser is chrome *****");
                break;
            }
            case "chrome-headless": {
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("start-maximized");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--headless=new");
                driver = new ChromeDriver(chromeOptions);
                logger.info("***** Browser is chrome-headless *****");
                break;
            }
        }
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriverThreadLocal.set(driver);
        System.out.println("Created browser for Thread " + Thread.currentThread().getId());
        return webDriverThreadLocal.get();
    }

    public static WebDriver getCurrentDriver() {
        return webDriverThreadLocal.get();
    }

    public static void closeBrowser() {
        webDriverThreadLocal.get().close();
        webDriverThreadLocal.get().quit();
        System.out.println("Closing browser for Thread " + Thread.currentThread().getId());
        webDriverThreadLocal.remove();
    }
}
