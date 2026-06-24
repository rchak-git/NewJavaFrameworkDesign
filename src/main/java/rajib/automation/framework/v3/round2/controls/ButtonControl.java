package rajib.automation.framework.v3.round2.controls;

import org.openqa.selenium.WebElement;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.enums.ActionType;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;
import rajib.automation.framework.v3.round2.utils.DriverActions;

public class ButtonControl extends BaseControl {

    public ButtonControl(FieldSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }

    private FieldSchema fieldSchema() {
        return (FieldSchema) schema;
    }

    @Override
    public void populate(ControlCommand command) {
        WebElement element = resolver.resolve(fieldSchema());
        DriverActions.click(element);
        System.out.println("BUTTON CLICKED (populate): " + command.getFieldKey());
    }

    @Override
    public void verify(ControlCommand command) {
        WebElement element = resolver.resolve(fieldSchema());
        if (!element.isDisplayed() || !element.isEnabled()) {
            throw new AssertionError("Button not interactable: " + command.getFieldKey());
        }
        System.out.println("BUTTON VERIFIED AS DISPLAYED AND ENABLED: " + command.getFieldKey());
    }

    @Override
    public Object read() {
        return resolver.resolve(fieldSchema()).getText();
    }

    @Override
    public void doAction(ControlCommand command) {
        WebElement element = resolver.resolve(fieldSchema());

        ActionType actionType =
                (command.getType() instanceof ActionType)
                        ? (ActionType) command.getType()
                        : ActionType.CLICK;

        switch (actionType) {
            case CLICK -> {
                DriverActions.click(element);
                System.out.println("BUTTON 'doAction': CLICK for " + command.getFieldKey());
            }
            case DOUBLE_CLICK -> {
                DriverActions.doubleClick(element);
                System.out.println("BUTTON 'doAction': DOUBLE_CLICK for " + command.getFieldKey());
            }
            default -> throw new UnsupportedOperationException("Button does not support action: " + actionType);
        }
    }
}