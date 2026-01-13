package core.context;


import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;

public final class StepContext {

    private final WebDriver driver;
    private final String testName;
    private final ExtentTest extentTest;

    public StepContext(WebDriver driver, String testName, ExtentTest extentTest) {
        this.driver = driver;
        this.testName = testName;
        this.extentTest = extentTest;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getTestName() {
        return testName;
    }

    public ExtentTest getExtentTest() {
        return extentTest;
    }
}
