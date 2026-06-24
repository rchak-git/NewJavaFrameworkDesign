package rajib.automation.framework.v3.round2.controls;

import org.openqa.selenium.WebElement;
import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;
import rajib.automation.framework.v3.round2.utils.DriverActions;

public class RadioGroupControl extends BaseControl {

    public RadioGroupControl(FieldSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }

    private FieldSchema fieldSchema() {
        return (FieldSchema) schema;
    }

    @Override
    public void populate(ControlCommand command) {
        String valueKey = String.valueOf(command.getValue());
        WebElement radioInput = resolver.resolve(fieldSchema(), valueKey);
        DriverActions.click(radioInput);
        System.out.println("Radio option selected: " + valueKey + " for field " + command.getFieldKey());
    }

    @Override
    public void verify(ControlCommand command) {
        String expectedKey = String.valueOf(command.getValue());
        Object typeObj = command.getType();
        ValidationType type = null;
        if (typeObj instanceof ValidationType) {
            type = (ValidationType) typeObj;
        } else if (typeObj instanceof String && typeObj != null) {
            type = ValidationType.valueOf((String) typeObj);
        }
        if (type == null) {
            type = ValidationType.TEXT_EQUALS;
        }
    }

    @Override
    public Object read() {
        FieldSchema fieldSchema = fieldSchema();

        for (String optionKey : fieldSchema.getLocators().keySet()) {
            WebElement radioInput = resolver.resolve(fieldSchema, optionKey);
            if (radioInput.isSelected()) {
                return optionKey;
            }
        }
        return null;
    }
}