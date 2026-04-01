package rajib.automation.framework.v3.round2.factory;

import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.Control;
import rajib.automation.framework.v3.round2.controls.CheckboxControl;
import rajib.automation.framework.v3.round2.controls.TextBoxControl;
import rajib.automation.framework.v3.round2.controls.DualListBoxControl; // <-- NEW
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

public class ControlFactory {

    private final ElementResolver resolver;

    public ControlFactory(ElementResolver resolver) {
        this.resolver = resolver;
    }

    // STATIC method for backward compatibility (could be deprecated later)
    public static BaseControl getControl(FieldSchema schema) {
        ElementResolver resolver = new ElementResolver();
        // TEMP: still always returns TextBox (can be deprecated/removed later)
        return new TextBoxControl(schema, resolver);
    }

    // INSTANCE method: use this for all future instantiation (preferred)
    public Control create(FieldSchema schema) {
        FieldType type = schema.fieldType();

        switch (type) {
            case TEXTBOX:
                return new TextBoxControl(schema, resolver);
            case CHECKBOX:
                return new CheckboxControl(schema, resolver);
            case DUALLISTBOX:
                return new DualListBoxControl(schema, resolver); // <-- NEW: dual list box support
            // Add more cases for future controls
            default:
                throw new UnsupportedOperationException(
                        "ControlFactory: FieldType not supported yet → " + type
                );
        }
    }
}