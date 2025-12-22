package rajib.automation.framework.base;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.utils.ConfigReader;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.Properties;

public class BaseTest {

    protected WebDriver driver;
    protected Properties prop;

    protected WebDriverWait wait ;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        prop = ConfigReader.loadProperties();
        String browser = prop.getProperty("browser");

        DriverFactory.setDriver(browser);
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        driver.get(prop.getProperty("qaUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
