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

    @Override
    public void populate(ControlCommand command) {
        Object value = command.getValue();

        if (value instanceof Boolean boolVal) {
            // Rare: use if the field is a single checkbox
            setChecked(boolVal);
        } else if (value instanceof String s) {
            // "hobbies": "Sports"
            selectCheckboxOption(s, true);
        } else if (value instanceof List<?> list) {
            // "hobbies": ["Sports", "Music"]
            for (Object v : list) {
                if (v != null) {
                    selectCheckboxOption(String.valueOf(v), true);
                }
            }
        } else if (value == null) {
            // Do nothing or uncheck all (optional by your framework needs)
        } else {
            throw new IllegalArgumentException("Unsupported value for checkbox: " + value + " (class: " + value.getClass() + ")");
        }
    }

    // Helper for single-checkbox situations (rare for "grouped" case)
    protected void setChecked(boolean checked) {
        WebElement checkbox = resolver.resolve(schema); // main locator by default
        if (checkbox.isSelected() != checked) {
            checkbox.click();
        }
    }

    // For group checkboxes, locate by the lower-cased, trimmed label (key in schema.locators)
    protected void selectCheckboxOption(String label, boolean checked) {
        String normalizedLabel = label.trim().toLowerCase();
        LocatorSchema locator = schema.locators.get(normalizedLabel);
        if (locator == null) {
            throw new IllegalArgumentException("Locator not found for label: " + normalizedLabel
                    + ". Available: " + schema.locators.keySet());
        }
        WebElement checkbox = resolver.resolve(schema, normalizedLabel);
        if (checkbox.isSelected() != checked) {
            checkbox.click();
        }
    }

    @Override
    public Object read() {
        // For a single checkbox, this makes sense. For a group, override as needed.
        return resolveElement().isSelected();
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