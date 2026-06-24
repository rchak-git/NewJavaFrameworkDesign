package rajib.automation.framework.v3.round2.controls;

import org.openqa.selenium.WebElement;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import java.util.List;

public class CheckboxControl extends BaseControl {

    public CheckboxControl(FieldSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }

    private FieldSchema fieldSchema() {
        return (FieldSchema) schema;
    }

    @Override
    public void populate(ControlCommand command) {
        Object value = command.getValue();

        if (value instanceof Boolean boolVal) {
            setChecked(boolVal);
        } else if (value instanceof String s) {
            selectCheckboxOption(s, true);
        } else if (value instanceof List<?> list) {
            for (Object v : list) {
                if (v != null) {
                    selectCheckboxOption(String.valueOf(v), true);
                }
            }
        } else if (value == null) {
            // no-op
        } else {
            throw new IllegalArgumentException("Unsupported value for checkbox: " + value + " (class: " + value.getClass() + ")");
        }
    }

    protected void setChecked(boolean checked) {
        WebElement checkbox = resolver.resolve(fieldSchema());
        if (checkbox.isSelected() != checked) {
            checkbox.click();
        }
    }

    protected void selectCheckboxOption(String label, boolean checked) {
        FieldSchema fieldSchema = fieldSchema();
        String normalizedLabel = label.trim().toLowerCase();
        LocatorSchema locator = fieldSchema.locators.get(normalizedLabel);
        if (locator == null) {
            throw new IllegalArgumentException("Locator not found for label: " + normalizedLabel
                    + ". Available: " + fieldSchema.locators.keySet());
        }

        WebElement checkbox = resolver.resolve(fieldSchema, normalizedLabel);
        if (checkbox.isSelected() != checked) {
            checkbox.click();
        }
    }

    @Override
    public Object read() {
        return resolver.resolve(fieldSchema()).isSelected();
    }

    @Override
    public void verify(ControlCommand command) {
        boolean expected = (Boolean) command.getValue();
        boolean actual = (Boolean) read();
        if (expected != actual) {
            throw new AssertionError(
                    "IS_SELECTED failed for field '" + command.getFieldKey() +
                            "' | Expected: " + expected + " | Actual: " + actual
            );
        }
        System.out.println("VERIFY PASSED for " + command.getFieldKey());
    }
}