package rajib.automation.framework.v3.round2.engine;

import com.aventstack.extentreports.ExtentTest;
import core.context.StepContext;
import rajib.automation.framework.v3.round2.page.BasePageR2;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.enums.ControlAction;
import rajib.automation.framework.v3.round2.control.Control;
import reporting.StepReporter;

import java.util.List;

/**
 * Flexibly dispatches ControlCommands to a Page's controls,
 * supporting both test-data-driven and code-driven approaches.
 */
public class CommandDispatcherR2 {

    /**
     * Executes a list of ControlCommands on the provided BasePageR2,
     * dispatching to the correct method per command action.
     *
     * @param page     The Page Object (BasePageR2 or subclass)
     * @param commands List of ControlCommand (from ScenarioNormalizerR2 or ad hoc)
     */
    public static void executeAll(BasePageR2 page, List<ControlCommand> commands, StepContext context) {
        for (ControlCommand command : commands) {
            String key = command.getFieldKey();
            ControlAction action = command.getAction();

            // ---- Step 1: Build a readable step name ----
            String stepName = String.format("%s - %s", action, key);

            // ---- Step 2: Create a step node in Extent under the current test ----
            ExtentTest parentTest = context.getExtentTest(); // <-- provided by your StepContext!
            ExtentTest stepNode = StepReporter.startStep(parentTest, stepName);

            long start = System.currentTimeMillis();
            try {
                Control control = page.getControl(key);

                switch (action) {
                    case POPULATE -> control.populate(command);
                    case VERIFY   -> control.verify(command);
                    case ACTION   -> control.doAction(command);
                    default -> throw new UnsupportedOperationException(
                            "Unknown ControlAction: " + action);
                }
                long duration = System.currentTimeMillis() - start;

                // ---- Step 3: Mark step as passed ----
                StepReporter.pass(stepNode, duration);
            } catch (Exception | Error ex) {
                long duration = System.currentTimeMillis() - start;

                // ---- Step 4: Mark step as failed ----
                StepReporter.fail(stepNode, ex, duration);

                // Optional: add extra details to the report, or rethrow for test failure
                throw ex;
            }
        }
    }

    // === Escape hatch: direct API for test authors ===

    /**
     * Executes a set of commands for the given page, optionally with context for future expansion.
     *
     * Note: 'context' parameter is not yet used, but is included for future compatibility when executeAll and
     * downstream step/command logic are refactored to support context-based reporting (logging, etc).
     */
    public static void executeAdHoc(BasePageR2 page, StepContext context, ControlCommand... adHocCommands) {
        // 'context' is available if you want to use it elsewhere in this method,
        // but as 'executeAll' does not accept it, do not pass it here.
        executeAll(page, java.util.Arrays.asList(adHocCommands),context);
        // Any context-related step reporting should be handled
        // in your per-step/command logic using ExtentTestManager.getTest().
    }
}