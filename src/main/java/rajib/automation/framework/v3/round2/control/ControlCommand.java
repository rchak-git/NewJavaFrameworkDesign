package rajib.automation.framework.v3.round2.control;

import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.v3.round2.enums.ControlAction;

public class ControlCommand {

    private ControlAction action;
    private String fieldKey;
    private Object value;
    private ValidationType validationType;   // ✅ NEW

    // Constructor for POPULATE
    public ControlCommand(ControlAction action, String fieldKey, Object value) {
        this.action = action;
        this.fieldKey = fieldKey;
        this.value = value;
    }

    // Constructor for VERIFY
    public ControlCommand(ControlAction action,
                          String fieldKey,
                          Object value,
                          ValidationType validationType) {
        this.action = action;
        this.fieldKey = fieldKey;
        this.value = value;
        this.validationType = validationType;
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

    public ValidationType getValidationType() {
        return validationType;
    }

    @Override
    public String toString() {
        return "ControlCommand{" +
                "action=" + action +
                ", fieldKey='" + fieldKey + '\'' +
                ", value=" + value +
                ", validationType=" + validationType +
                '}';
    }
}