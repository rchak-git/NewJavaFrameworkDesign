package rajib.automation.framework.v3.round2.control;

import rajib.automation.framework.v3.round2.enums.ControlAction;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Generalized command to dispatch all control operations (populate/verify/action).
 *
 * Core fields stay stable, while widget- or keyword-specific data lives in attributes.
 */
public class ControlCommand {
    private final ControlAction action;
    private final String fieldKey;
    private final Object value;
    private final Object type; // Can be ValidationType, PopulationType, ActionType, etc.
    private final Map<String, Object> attributes;

    private String waitForOption; // Null unless you want an option wait

    // Generic constructor: always provide action, field, value, and type (type may be null)
    public ControlCommand(ControlAction action, String fieldKey, Object value, Object type) {
        this(action, fieldKey, value, type, null);
    }

    public ControlCommand(ControlAction action, String fieldKey, Object value, Object type, Map<String, Object> attributes) {
        this.action = action;
        this.fieldKey = fieldKey;
        this.value = value;
        this.type = type;
        this.attributes = attributes == null ? new LinkedHashMap<>() : new LinkedHashMap<>(attributes);
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

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public ControlCommand withAttribute(String name, Object value) {
        attributes.put(name, value);
        return this;
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
                ", attributes=" + attributes +
                '}';
    }
}
