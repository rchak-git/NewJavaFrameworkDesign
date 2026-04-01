package rajib.automation.framework.v3.round2.control;


import rajib.automation.framework.intent.VerifySpec;

public interface Control {
    void populate(ControlCommand command);
    void verify(ControlCommand command);

    Object read();

    // NEW: This enables custom widget-specific actions like ADD, REMOVE, etc.
    default void doAction(ControlCommand command) {
        throw new UnsupportedOperationException(
                "Custom action not supported: " + command.getAction()
        );
    }
}