package rajib.automation.framework.steps;


import com.aventstack.extentreports.ExtentTest;
import core.context.StepContext;
import reporting.StepReporter;
import rajib.automation.framework.steps.StepLog;


public final class Steps {

    private Steps() {}

    public static StepResult run(StepContext ctx, String stepName, StepAction action) {

        long start = System.currentTimeMillis();
        System.out.println("[STEP START] " + ctx.getTestName() + " :: " + stepName);

        ExtentTest stepNode =
                StepReporter.startStep(ctx.getExtentTest(), stepName);

        StepLog.bind(stepNode);

        try {
            action.run();

            long duration = System.currentTimeMillis() - start;
            StepReporter.pass(stepNode, duration);

            System.out.println("[STEP PASS ] " + ctx.getTestName()
                    + " :: " + stepName + " (" + duration + " ms)");

            return StepResult.pass(stepName, duration);

        } catch (RuntimeException | Error t) {

            long duration = System.currentTimeMillis() - start;
            StepReporter.fail(stepNode, t, duration);

            System.out.println("[STEP FAIL ] " + ctx.getTestName()
                    + " :: " + stepName + " (" + duration + " ms)");

            StepResult result = StepResult.fail(stepName, duration, t);
            throw t;
        } catch (Exception e) {

            long duration = System.currentTimeMillis() - start;
            StepReporter.fail(stepNode, e, duration);

            throw new RuntimeException(e);   // wrap checked exceptions
        }finally {
            StepLog.unbind();
        }
    }
}



