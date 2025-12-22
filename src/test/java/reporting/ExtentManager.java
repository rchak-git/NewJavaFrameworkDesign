package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {
        // prevent instantiation
    }

    public static ExtentReports getExtentReports() {

        if (extent == null) {

            ExtentSparkReporter sparkReporter =
                    new ExtentSparkReporter("reports/extent-report.html");

            sparkReporter.config().setReportName("Automation Test Execution Report");
            sparkReporter.config().setDocumentTitle("Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Optional metadata (nice to have)
            extent.setSystemInfo("Framework", "Java Selenium");
            extent.setSystemInfo("Execution Type", "Local");

        }

        return extent;
    }
}
