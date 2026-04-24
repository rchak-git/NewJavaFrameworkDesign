package rajib.automation.framework.v3.round2.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;
import rajib.automation.framework.v3.round2.utils.DriverActions;

import java.util.List;
import java.util.Map;

public class RadioGroupControl extends BaseControl {

    public RadioGroupControl(FieldSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }

    @Override
    public void populate(ControlCommand command) {
        // The value in command's test data should match locator key in schema (e.g., "male", "female", "other")
        String valueKey = String.valueOf(command.getValue());
        WebElement radioInput = resolver.resolve(schema, valueKey);
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
        // We'll flesh out the switch/case and validation logic in the next step!
    }
    @Override
    public Object read() {
        // Return the key of the selected option for this radio group, or null if none selected
        for (String optionKey : schema.getLocators().keySet()) {
            WebElement radioInput = resolver.resolve(schema, optionKey);
            if (radioInput.isSelected()) {
                return optionKey;
            }
        }
        return null;
    }
}