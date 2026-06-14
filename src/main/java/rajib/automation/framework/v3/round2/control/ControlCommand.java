package rajib.automation.framework.v3.round2.control;

import rajib.automation.framework.v3.round2.enums.ControlAction;

/**
 * Generalized command to dispatch all control operations (populate/verify/action).
 * The 'type' field is a flex slot for ValidationType, PopulationType, ActionType, etc.
 */
public class ControlCommand {
    private final ControlAction action;
    private final String fieldKey;
    private final Object value;
    private final Object type; // Can be ValidationType, PopulationType, ActionType, etc.

    private String waitForOption; // Null unless you want an option wait

    // Generic constructor: always provide action, field, value, and type (type may be null)
    public ControlCommand(ControlAction action, String fieldKey, Object value, Object type) {
        this.action = action;
        this.fieldKey = fieldKey;
        this.value = value;
        this.type = type;
    }


    public String getWaitForOption() {
        return waitForOption;
    }

    public void setWaitForOption(String waitForOption) {
        this.waitForOption = waitForOption;
    }

    // Convenience constructor for simple legacy POPULATE/VERIFY (no type needed)
    public ControlCommand(ControlAction action, String fieldKey, Object value) {
        this(action, fieldKey, value, null);
    }

    public ControlAction getAction() {
        return action;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public Object getValue() {
        return value;
    }

    // Main "type" getter (downcast as needed)
    public Object getType() {
        return type;
    }

    // Convenience: Cast "type" to ValidationType
    public <T> T asType(Class<T> clazz) {
        return clazz.cast(type);
    }

    @Override
    public String toString() {
        return "ControlCommand{" +
                "action=" + action +
                ", fieldKey='" + fieldKey + '\'' +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}