package rajib.automation.framework.v3.round2.factory;

import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.Control;
import rajib.automation.framework.v3.round2.controls.CheckboxControl;
import rajib.automation.framework.v3.round2.controls.TextBoxControl;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

public class ControlFactory {

    private final ElementResolver resolver;

    public ControlFactory(ElementResolver resolver) {
        this.resolver = resolver;
    }

    public static BaseControl getControl(FieldSchema schema) {

        ElementResolver resolver = new ElementResolver();

        // 🔥 TEMP: assume textbox
        return new TextBoxControl(schema, resolver);
    }
    public Control create(FieldSchema schema) {

        FieldType type = schema.fieldType();

        switch (type) {

            case TEXTBOX:
                return new TextBoxControl(schema, resolver);
            case CHECKBOX:
                return new CheckboxControl(schema, resolver);

            default:
                throw new UnsupportedOperationException(
                        "ControlFactory: FieldType not supported yet → " + type
                );
        }
    }
}