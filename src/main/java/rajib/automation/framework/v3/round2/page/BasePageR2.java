package rajib.automation.framework.v3.round2.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.Control;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.factory.ControlFactory;

public class BasePageR2 {

    private final Map<String, Control> controls = new HashMap<>();
    private final List<FieldSchema> fieldSchemas;

    private final ControlFactory factory;
    public BasePageR2(List<FieldSchema> fieldSchemas,
                      ControlFactory factory) {
        this.fieldSchemas = fieldSchemas;  // ✅ store it
        this.factory = factory;
        for (FieldSchema schema : fieldSchemas) {

            Control control = factory.create(schema);

            controls.put(schema.key(), control);
        }
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


    private FieldSchema getSchema(String fieldKey) {

        return fieldSchemas.stream()
                .filter(schema -> schema.key().equals(fieldKey))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No FieldSchema found for key: " + fieldKey
                ));
    }
    public void execute(List<ControlCommand> commands) throws InterruptedException {

        for (ControlCommand command : commands) {
           Thread.sleep(500);
           Control control = controls.get(command.getFieldKey());
            if (control == null) {
                continue;
            }


            switch (command.getAction()) {

                case POPULATE ->
                        control.populate(command);

                case VERIFY ->
                        control.verify(command);
                case ADD, REMOVE, ADD_ALL, REMOVE_ALL -> control.doAction(command);
                default -> throw new UnsupportedOperationException(
                        "Action not supported: " + command.getAction());
            }
        }
    }
}