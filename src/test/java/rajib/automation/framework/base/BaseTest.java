package rajib.automation.framework.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.utils.ConfigReader;
import rajib.automation.framework.v2.context.RuntimeContextHolder;
import reporting.ExtentManager;
import reporting.ExtentTestManager;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Properties;

public abstract class BaseTest {

    protected WebDriver driver;
    protected Properties prop;
    protected WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        // Existing setup (driver etc.)
        prop = ConfigReader.loadProperties();
        String browser = prop.getProperty("browser");
        DriverFactory.setDriver(browser);
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
      //  driver.get(prop.getProperty("qaUrl"));
        RuntimeContextHolder.clear();
        ExtentManager.getExtentReports();
        // Step 1: Create test entry in ExtentReports for this test method
        ExtentTestManager.createTest(method.getName());
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Set status in ExtentReport for this test
        if (result.getStatus() == ITestResult.FAILURE) {
            ExtentTestManager.getTest().fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentTestManager.getTest().skip(result.getThrowable());
        } else {
            ExtentTestManager.getTest().pass("Test passed!");
        }

        // Remove the test instance (ThreadLocal cleanup)
        ExtentTestManager.removeTest();

        // --- Add this block to flush the report file! ---
        if (ExtentManager.getExtentReports() != null) {
            ExtentManager.getExtentReports().flush();
        }

        DriverFactory.quitDriver();
        RuntimeContextHolder.clear();
    }

    // The executeIntent methods can be removed unless still used.
}