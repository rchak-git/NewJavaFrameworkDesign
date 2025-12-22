package reporting;

import com.aventstack.extentreports.Status;

/**
 * ExtentReportSink
 *
 * Extent-specific implementation of ReportSink.
 * Lives in test layer because it depends on Extent + TestNG lifecycle.
 */
public class ExtentReportSink implements ReportSink {

    @Override
    public void info(String message) {
        ExtentTestManager.getTest().log(Status.INFO, message);
    }

    @Override
    public void warning(String message) {
        ExtentTestManager.getTest().log(Status.WARNING, message);
    }

    @Override
    public void pass(String message) {
        ExtentTestManager.getTest().log(Status.PASS, message);
    }

    @Override
    public void fail(String message) {
        ExtentTestManager.getTest().log(Status.FAIL, message);
    }
}
