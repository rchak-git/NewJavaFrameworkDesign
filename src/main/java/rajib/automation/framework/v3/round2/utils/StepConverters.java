package rajib.automation.framework.v3.round2.utils;

import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.context.RuntimeContextHolder;
import rajib.automation.framework.v2.resolver.PlaceholderResolver;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.enums.ControlAction;
import rajib.automation.framework.v3.round2.testdatamodels.TestStepData;

import java.util.LinkedHashMap;
import java.util.Map;

public class StepConverters {

    public static ControlCommand fromTestStepData(TestStepData step) {
        ControlAction action = ControlAction.valueOf(step.intent().toUpperCase());
        RuntimeContext context = RuntimeContextHolder.get();

        return switch (action) {
            case POPULATE -> {
                Object resolvedValue = PlaceholderResolver.resolve(
                        Map.of("value", step.value()),
                        context
                ).get("value");
                yield new ControlCommand(action, step.fieldKey(), resolvedValue);
            }
            case VERIFY -> {
                Object resolvedExpected = PlaceholderResolver.resolve(
                        Map.of("expected", step.expected()),
                        context
                ).get("expected");
                ValidationType valType = ValidationType.DEFAULT;
                String valTypeStr = step.validationType();
                if (valTypeStr != null && !valTypeStr.isEmpty()) {
                    valType = ValidationType.valueOf(valTypeStr);
                }

                Map<String, Object> attributes = new LinkedHashMap<>();
                if (step.matchBy() != null) {
                    attributes.put("matchBy", PlaceholderResolver.resolve(
                            Map.of("matchBy", step.matchBy()),
                            context
                    ).get("matchBy"));
                }
                attributes.put("expected", resolvedExpected);

                yield new ControlCommand(action, step.fieldKey(), resolvedExpected, valType, attributes);
            }
            case ACTION -> {
                String actTypeStr = step.actionType() != null ? step.actionType() : "CLICK";
                rajib.automation.framework.v3.round2.enums.ActionType actType = rajib.automation.framework.v3.round2.enums.ActionType.valueOf(actTypeStr.toUpperCase());
                yield new ControlCommand(action, step.fieldKey(), null, actType);
            }
            default -> throw new IllegalArgumentException("Unsupported action: " + action);
        };
    }
}
