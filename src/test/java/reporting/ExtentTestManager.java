package reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ExtentReports;

public class ExtentTestManager {

    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private ExtentTestManager() {
        // prevent instantiation
    }

    public static void createTest(String testName) {
        ExtentReports extent = ExtentManager.getExtentReports();
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void removeTest() {
        test.remove();
    }
}
