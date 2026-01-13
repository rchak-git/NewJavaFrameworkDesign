package reporting;


import com.aventstack.extentreports.ExtentTest;

public final class StepReporter {

    private StepReporter() {}

    public static ExtentTest startStep(ExtentTest parent, String stepName) {
        return parent.createNode(stepName);
    }

    public static void pass(ExtentTest stepNode, long durationMs) {
        stepNode.pass("Step passed (" + durationMs + " ms)");
    }

    public static void fail(ExtentTest stepNode, Throwable t, long durationMs) {
        stepNode.fail("Step failed (" + durationMs + " ms)");
        stepNode.fail(t);
    }
}

