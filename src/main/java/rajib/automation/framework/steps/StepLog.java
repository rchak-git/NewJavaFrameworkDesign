package rajib.automation.framework.steps;


import com.aventstack.extentreports.ExtentTest;

public final class StepLog {

    private static final ThreadLocal<ExtentTest> CURRENT_STEP = new ThreadLocal<>();

    private StepLog() {}

    static void bind(ExtentTest stepNode) {
        CURRENT_STEP.set(stepNode);
    }

    static void unbind() {
        CURRENT_STEP.remove();
    }

    public static void info(String message) {
        ExtentTest step = CURRENT_STEP.get();
        if (step != null) {
            step.info(message);
        }
    }
}
