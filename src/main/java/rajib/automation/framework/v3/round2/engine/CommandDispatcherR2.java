package rajib.automation.framework.v3.round2.engine;

import com.aventstack.extentreports.ExtentTest;
import core.context.StepContext;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.context.RuntimeContextHolder;
import rajib.automation.framework.v2.resolver.PlaceholderResolver;
import rajib.automation.framework.v3.round2.control.Control;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.enums.ControlAction;
import rajib.automation.framework.v3.round2.page.BasePageR2;
import reporting.StepReporter;

import java.util.List;

public class CommandDispatcherR2 {

    public static void execute(BasePageR2 page, List<ControlCommand> commands, StepContext context) {
        for (ControlCommand command : commands) {
            dispatchSingle(page, command, context);
        }
    }

    private static void dispatchSingle(BasePageR2 page, ControlCommand command, StepContext context) {
        String key = command.getFieldKey();
        ControlAction action = command.getAction();
        String stepName = String.format("%s - %s", action, key);

        ExtentTest parentTest = context.getExtentTest();
        ExtentTest stepNode = StepReporter.startStep(parentTest, stepName);

        long start = System.currentTimeMillis();
        try {
            Control control = page.getControl(key);
            ControlCommand resolvedCommand = resolveCommand(command);

            switch (action) {
                case POPULATE -> control.populate(resolvedCommand);
                case VERIFY -> control.verify(resolvedCommand);
                case ACTION -> control.doAction(resolvedCommand);
                default -> throw new UnsupportedOperationException(
                        "Unknown ControlAction: " + action);
            }

            long duration = System.currentTimeMillis() - start;
            StepReporter.pass(stepNode, duration);
        } catch (Exception | Error ex) {
            long duration = System.currentTimeMillis() - start;
            StepReporter.fail(stepNode, ex, duration);
            throw ex;
        }
    }

    private static ControlCommand resolveCommand(ControlCommand command) {
        RuntimeContext runtimeContext = RuntimeContextHolder.get();

        Object resolvedValue = PlaceholderResolver.resolveValue(command.getValue(), runtimeContext);
        Object resolvedType = PlaceholderResolver.resolveValue(command.getType(), runtimeContext);

        ControlCommand resolved = new ControlCommand(
                command.getAction(),
                command.getFieldKey(),
                resolvedValue,
                resolvedType
        );
        resolved.setWaitForOption(command.getWaitForOption());
        return resolved;
    }

    public static void executeAdHoc(BasePageR2 page, StepContext context, ControlCommand... adHocCommands) {
        for (ControlCommand command : adHocCommands) {
            dispatchSingle(page, command, context);
        }
    }
}