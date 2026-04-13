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

    private final Map<String, Control> controls = new HashMap<>();

    protected final ElementResolver resolver;
    protected final RuntimeContext context;



    public BasePageR2(ElementResolver resolver, RuntimeContext context) {
        this.resolver = resolver;
        this.context = context;
    }
    // ---- Codegen pattern: controls are added from page class, not by schema/factory ----


    // ---- (Optional legacy pattern for runtime schema/factory injection)
    // If you wish to keep legacy support, you can uncomment the below:
    /*
    private final List<FieldSchema> fieldSchemas;
    private final ControlFactory factory;

    public BasePageR2(List<FieldSchema> fieldSchemas,
                      ControlFactory factory) {
        this.fieldSchemas = fieldSchemas;  // ✅ store it
        this.factory = factory;
        for (FieldSchema schema : fieldSchemas) {
            Control control = factory.create(schema);
            controls.put(schema.key, control);
        }
    }
    */
    // ---- End legacy region ----

    /** Register a control (should only be used in codegen constructor in page class) */
    protected void registerControl(String key, Control control) {
        controls.put(key, control);
    }

    public Control getControl(String key) {
        Control control = controls.get(key);
        if (control == null) {
            throw new IllegalArgumentException(
                    "No control registered for key: " + key
            );
        }
        return control;
    }

    public Map<String, Control> getControls() {
        return controls;
    }


    public void populateField(ControlCommand command) {
        Control control = getControl(command.getFieldKey());
        if (control == null) throw new IllegalArgumentException(
                "No control found for fieldKey: " + command.getFieldKey());
        control.populate(command);
    }
    public Object readField(String fieldKey) {
        Control control = getControl(fieldKey);
        if (control == null) {
            throw new IllegalArgumentException("No control found for fieldKey: " + fieldKey);
        }
        return control.read();
    }





    /** Now deprecated! If you remove legacy fields above, this can be removed as well. */
    /*
    private FieldSchema getSchema(String fieldKey) {
        return fieldSchemas.stream()
                .filter(schema -> schema.key.equals(fieldKey))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No FieldSchema found for key: " + fieldKey
                ));
    }
    */

    public void execute(List<ControlCommand> commands) throws InterruptedException {
        for (ControlCommand command : commands) {
            Thread.sleep(500);
            Control control = controls.get(command.getFieldKey());
            if (control == null) {
                continue;
            }
            switch (command.getAction()) {
                case POPULATE -> control.populate(command);
                case VERIFY -> control.verify(command);
                // case ADD, REMOVE, ADD_ALL, REMOVE_ALL -> control.doAction(command);
                default -> throw new UnsupportedOperationException(
                        "Action not supported: " + command.getAction());
            }
        }
    }
}