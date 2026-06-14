package rajib.automation.framework.v3.round2.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.Control;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.enums.ControlAction;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

public class BasePageR2 {
    // You could use <String, BaseControl> if you always extend BaseControl
    private final Map<String, Control> controls = new HashMap<>();

    protected final ElementResolver resolver;
    protected final RuntimeContext context;

    public BasePageR2(ElementResolver resolver, RuntimeContext context) {
        this.resolver = resolver;
        this.context = context;
    }

    /** Register a single control instance under its key. */
    protected void registerControl(String key, Control control) {
        controls.put(key, control);
    }

    /** Optional quality-of-life: bulk registration (called from child) */
    protected void registerControls(Map<String, Control> controlsToAdd) {
        controls.putAll(controlsToAdd);
    }

    /** Retrieve a Control by key or throw for missing key. */
    public Control getControl(String key) {
        Control control = controls.get(key);
        if (control == null) {
            throw new IllegalArgumentException("No control registered for key: " + key);
        }
        return control;
    }

    public Map<String, Control> getControls() {
        return controls;
    }

    /**
     * Populates a field using the correct Control type—waits for async controls can go here.
     * Enhance: add a pre-populate wait hook (see below)
     */
    public void populateField(ControlCommand command) {
        Control control = getControl(command.getFieldKey());
        applyWaitIfNeeded(command, control);
        control.populate(command);
    }

    public Object readField(String fieldKey) {
        Control control = getControl(fieldKey);
        return control.read();
    }

    /**
     * The main runner for a list of commands.
     * Throws if a field in the command list is missing. (Can be reverted to continue if desired.)
     */
    public void execute(List<ControlCommand> commands) throws InterruptedException {
        for (ControlCommand command : commands) {
            Thread.sleep(500); // You can parameterize this timeout.
            Control control = getControl(command.getFieldKey());
            applyWaitIfNeeded(command, control);
            switch (command.getAction()) {
                case POPULATE -> control.populate(command);
                case VERIFY -> control.verify(command);
                // Add other actions as needed.
                default -> throw new UnsupportedOperationException(
                        "Action not supported: " + command.getAction());
            }
        }
    }

    /**
     * Extension point for async/wait: generic wait logic for controls with dependencies.
     * Overwrite or expand this for project- or control-specific waits.
     */
    protected void applyWaitIfNeeded(ControlCommand command, Control control) {
        // Example: check if the ControlCommand or schema indicates a wait, and perform it.
        // For your react-select City dropdown after State, you can set a custom wait condition in the command.
        String waitForOption = command.getWaitForOption();
        if (waitForOption != null && !waitForOption.isEmpty() && control instanceof BaseControl baseControl) {
        //-This line needs correction    BaseControl.WaitUtils.waitForDropdownOptionVisible(
           //         resolver.getDriver(),
         //-This line needs correction           baseControl.getLocator(), // or get a locator via schema
                //    waitForOption
          //  );
        }
        // For more advanced logic, check control type or schema meta here.
    }


}