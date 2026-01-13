package reporting;

import agent.FailureAnalysisAgent;
import com.aventstack.extentreports.ExtentReports;
import core.context.FailureContext;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import rajib.automation.framework.factory.DriverFactory;

public class ExtentTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTestManager.createTest(testName);
        ReportManager.registerSink(new ExtentReportSink());
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTestManager.getTest().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        Throwable throwable = result.getThrowable();

        String exceptionType = throwable != null
                ? throwable.getClass().getSimpleName()
                : null;

        String exceptionMessage = throwable != null
                ? throwable.getMessage()
                : null;

        // Capture browser context safely
        WebDriver driver = DriverFactory.getDriver();

        String pageUrl = null;
        String pageTitle = null;

        try {
            if (driver != null) {
                pageUrl = driver.getCurrentUrl();
                pageTitle = driver.getTitle();
            }
        } catch (Exception ignored) {
            // Do not fail listener
        }

        // Last known framework action (optional for now)
        String lastAction = "Unknown (action tracking not enabled yet)";

        // Build FailureContext
        FailureContext context = new FailureContext(
                exceptionType,
                exceptionMessage,
                pageUrl,
                pageTitle,
                lastAction
        );

        // Invoke agent
        FailureAnalysisAgent agent = new FailureAnalysisAgent();
        String agentInsight = agent.analyze(context);

        // Log agent insight as WARNING
        ReportManager.warning(agentInsight);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReports extent = ExtentManager.getExtentReports();
        extent.flush();
    }
}
