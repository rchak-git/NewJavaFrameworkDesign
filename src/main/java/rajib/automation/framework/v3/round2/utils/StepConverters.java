package rajib.automation.framework.v3.round2.utils;

import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.context.RuntimeContextHolder;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.enums.ControlAction;
import rajib.automation.framework.v3.round2.testdatamodels.TestStepData;

public class StepConverters {

    public static ControlCommand fromTestStepData(TestStepData step) {
        ControlAction action = ControlAction.valueOf(step.intent().toUpperCase());
        RuntimeContext context = RuntimeContextHolder.get();

        return switch (action) {
            case POPULATE -> {
                Object resolvedValue = resolvePopulateValue(step.value(), context);
                yield new ControlCommand(action, step.fieldKey(), resolvedValue);
            }
            case VERIFY -> {
                Object resolvedExpected = resolveVerifyExpected(step.expected(), context);
                ValidationType valType = ValidationType.DEFAULT;
                String valTypeStr = step.validationType();
                if (valTypeStr != null && !valTypeStr.isEmpty()) {
                    valType = ValidationType.valueOf(valTypeStr);
                }
                yield new ControlCommand(action, step.fieldKey(), resolvedExpected, valType);
            }
            case ACTION -> {
                // Use actionType from test data or default to CLICK
                String actTypeStr = step.actionType() != null ? step.actionType() : "CLICK";
                rajib.automation.framework.v3.round2.enums.ActionType actType = rajib.automation.framework.v3.round2.enums.ActionType.valueOf(actTypeStr.toUpperCase());
                yield new ControlCommand(action, step.fieldKey(), null, actType);
            }
            default -> throw new IllegalArgumentException("Unsupported action: " + action);
        };
    }

    // --- Context substitution logic for POPULATE ---
    private static Object resolvePopulateValue(Object value, RuntimeContext context) {
        if (isContextKey(value)) {
            String key = (String) value;
            Object actual;
            if (context.contains(key)) {
                actual = context.get(key);
            } else {
                // Add more cases as you add dynamic field types:
                if ("CTX_EMAIL".equals(key)) {
                    actual = generateEmail();
                } else if ("CTX_PHONE".equals(key)) {
                    actual = generatePhone();
                } else {
                    actual = java.util.UUID.randomUUID().toString();
                }
                context.put(key, actual);
            }
            return actual;
        }
        return value;
    }

    // --- Context substitution logic for VERIFY ---
    private static Object resolveVerifyExpected(Object expected, RuntimeContext context) {
        if (isContextKey(expected)) {
            String key = (String) expected;
            return context.get(key);
        }
        return expected;
    }

    private static boolean isContextKey(Object value) {
        return value instanceof String && ((String) value).startsWith("CTX_");
    }

    private static String generateEmail() {
        return "user" + System.currentTimeMillis() + "@example.com";
    }

    private static String generatePhone() {
        return "9" + ((long)(Math.random() * 1_000_000_000L));
    }
}