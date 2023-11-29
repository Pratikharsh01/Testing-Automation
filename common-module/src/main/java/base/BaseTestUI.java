package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.DriverManager;

public class BaseTestUI extends BaseTest {

    @BeforeClass
    @Parameters("browser")
    public void setup(String browser) {
        DriverManager.getDriver(browser);
    }

    public WebDriver getCurrentBrowser()
    {
        return DriverManager.getCurrentDriver();
    }

    @AfterClass
    public void teardown() {
        DriverManager.closeBrowser();
    }
}
