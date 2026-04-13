/*
DEPRECATED: This class is no longer used in the current design.
dynamic/context step normalization is now handled in StepConverters.
TODO: Remove this class and its usages after confirming all scenarios are properly handled by StepConverters and CommandDispatcherR2.
 */


package rajib.automation.framework.v3.round2.engine;

import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.context.RuntimeContextHolder;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.enums.ControlAction;
import rajib.automation.framework.v3.round2.enums.PopulationType;
import rajib.automation.framework.v3.round2.enums.ActionType;

import java.util.*;

public class ScenarioNormalizerR2 {

    /**
     * Returns a context-aware, normalized list of ControlCommands
     * for the given single-scenario map.
     */
    public List<ControlCommand> normalizeFlatScenario(Map<String, Object> scenario) {
        List<ControlCommand> commands = new ArrayList<>();
        RuntimeContext context = RuntimeContextHolder.get();

        for (Map.Entry<String, Object> entry : scenario.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                Map<String, Object> actions = (Map<String, Object>) value;

                // --- POPULATE ---
                if (actions.containsKey("populate")) {
                    Object popObj = actions.get("populate");
                    PopulationType popType = PopulationType.SET;
                    Object popValue = null;

                    if (popObj instanceof Map) {
                        Map<String, Object> popMap = (Map<String, Object>) popObj;
                        if (popMap.containsKey("pop_type")) {
                            popType = PopulationType.valueOf(
                                    ((String) popMap.get("pop_type")).toUpperCase()
                            );
                        }
                        popValue = popMap.get("value");
                    } else if (popObj != null) {
                        popType = PopulationType.SET;
                        popValue = popObj;
                    }

                    if (popType == PopulationType.CLEAR || popValue != null) {
                        Object finalValue = resolveContextValue(popValue, context);
                        commands.add(new ControlCommand(
                                ControlAction.POPULATE,
                                field,
                                finalValue,
                                popType
                        ));
                    }
                }

                // --- VERIFY ---
                if (actions.containsKey("verify")) {
                    Object verifyObj = actions.get("verify");
                    ValidationType valType = ValidationType.TEXT_EQUALS;
                    Object expectedValue = null;

                    if (verifyObj instanceof Map) {
                        Map<String, Object> verifyMap = (Map<String, Object>) verifyObj;
                        if (verifyMap.containsKey("valid_type")) {
                            valType = ValidationType.valueOf(
                                    ((String) verifyMap.get("valid_type")).toUpperCase()
                            );
                        }
                        expectedValue = verifyMap.get("expected");
                    } else if (verifyObj != null) {
                        valType = ValidationType.TEXT_EQUALS;
                        expectedValue = verifyObj;
                    }

                    if (expectedValue != null) {
                        Object finalExpected = resolveContextValueForVerify(expectedValue, context);
                        commands.add(new ControlCommand(
                                ControlAction.VERIFY,
                                field,
                                finalExpected,
                                valType
                        ));
                    }
                }

                // --- ACTION ---
                if (actions.containsKey("action")) {
                    Object actionObj = actions.get("action");
                    ActionType actionType = ActionType.CLICK; // Default (adjust as appropriate)
                    Object actionValue = null;

                    if (actionObj instanceof Map) {
                        Map<String, Object> actionMap = (Map<String, Object>) actionObj;
                        if (actionMap.containsKey("action_type")) {
                            actionType = ActionType.valueOf(
                                    ((String) actionMap.get("action_type")).toUpperCase()
                            );
                        }
                        actionValue = actionMap.get("value"); // If you ever need e.g. coordinates
                    } else if (actionObj != null) {
                        actionValue = actionObj;
                    }

                    // Handle dynamic context value if needed
                    Object finalActionValue = resolveContextValue(actionValue, context);

                    commands.add(new ControlCommand(
                            ControlAction.ACTION,
                            field,
                            finalActionValue,
                            actionType
                    ));
                }
            } else {
                // Simple field: treat as simple POPULATE (default SET)
                Object rawValue = resolveContextValue(value, context);

                commands.add(new ControlCommand(
                        ControlAction.POPULATE,
                        field,
                        rawValue,
                        PopulationType.SET
                ));
            }
        }
        return commands;
    }

    // Utility: Checks value for CTX_ prefix and creates or retrieves its value.
    private Object resolveContextValue(Object value, RuntimeContext context) {
        if (isContextKey(value)) {
            String key = (String) value;
            Object actual;
            if (context.contains(key)) {
                actual = context.get(key);
            } else {
                // Dynamic value generators (extend as needed)
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

    // Utility: For verify steps, only look up and return context value, never generate.
    private Object resolveContextValueForVerify(Object value, RuntimeContext context) {
        if (isContextKey(value)) {
            String key = (String) value;
            return context.get(key); // Will be null if not set, which is a test error!
        }
        return value;
    }

    private boolean isContextKey(Object value) {
        return value instanceof String && ((String) value).startsWith("CTX_");
    }

    // --- Sample stub dynamic generators ---
    private String generateEmail() {
        return "user" + System.currentTimeMillis() + "@example.com";
    }

    private String generatePhone() {
        return "9" + ((long)(Math.random()*1_000_000_000L));
    }
}