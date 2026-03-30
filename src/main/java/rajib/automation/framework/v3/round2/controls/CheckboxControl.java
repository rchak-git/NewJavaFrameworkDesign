package rajib.automation.framework.v3.round2.controls;

import org.openqa.selenium.WebElement;
import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

public class CheckboxControl extends BaseControl {

    public CheckboxControl(FieldSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }

    @Override
    public void populate(ControlCommand command) {

        boolean expected = (Boolean) command.getValue();

        WebElement element = resolveElement();

        boolean actual = element.isSelected();

        if (expected != actual) {
            element.click();
        }

        System.out.println("POPULATE CHECKBOX " + command.getFieldKey()
                + " = " + expected);
    }

    // ✅ NEW: implement read()
    @Override
    public Object read() {
        return resolveElement().isSelected();
    }

    @Override
    public void verify(ControlCommand command) {

        boolean expected = (Boolean) command.getValue();

        boolean actual = (Boolean) read();   // ✅ use abstraction

        if (expected != actual) {
            throw new AssertionError(
                    "IS_SELECTED failed for field '" + command.getFieldKey() +
                            "' | Expected: " + expected + " | Actual: " + actual
            );
        }

        System.out.println("VERIFY PASSED for " + command.getFieldKey());
    }
}