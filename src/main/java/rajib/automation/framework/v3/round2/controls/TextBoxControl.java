package rajib.automation.framework.v3.round2.controls;

import org.openqa.selenium.WebElement;
import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;
import rajib.automation.framework.v3.round2.utils.DriverActions;


public class TextBoxControl extends BaseControl {

    public TextBoxControl(FieldSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }

    @Override
    public void populate(ControlCommand command) {
        String value = String.valueOf(command.getValue());
        WebElement element = resolveElement();

        // Use DriverActions for robust input
        DriverActions.type(element, value);

        System.out.println("POPULATE " + command.getFieldKey() + " = " + value);
    }

    @Override
    public void verify(ControlCommand command) {
        String actual = String.valueOf(read());
        String expected = String.valueOf(command.getValue());
        ValidationType type = (ValidationType) command.getType();

        switch (type) {
            case DEFAULT, TEXT_EQUALS -> {
                if (!actual.equals(expected)) {
                    throw new AssertionError(
                            "TEXT_EQUALS failed for field '" + command.getFieldKey() +
                                    "' | Expected: " + expected + " | Actual: " + actual
                    );
                }
            }
            case TEXT_CONTAINS -> {
                if (!actual.contains(expected)) {
                    throw new AssertionError(
                            "TEXT_CONTAINS failed for field '" + command.getFieldKey() +
                                    "' | Expected to contain: " + expected + " | Actual: " + actual
                    );
                }
            }
            default -> throw new UnsupportedOperationException(
                    "Validation not supported: " + type
            );
        }

        System.out.println("VERIFY PASSED for " + command.getFieldKey());
    }
    @Override
    public Object read() {
        return resolveElement().getAttribute("value");
    }
}