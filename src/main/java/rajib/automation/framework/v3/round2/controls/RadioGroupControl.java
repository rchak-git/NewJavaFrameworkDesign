package rajib.automation.framework.v3.round2.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;
import rajib.automation.framework.v3.round2.utils.DriverActions;

import java.util.List;

public class RadioGroupControl extends BaseControl {

    public RadioGroupControl(FieldSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }

    @Override
    public void populate(ControlCommand command) {
        String valueToSelect = String.valueOf(command.getValue());

        // The parent locator should ideally find the group; adjust if schema gives only child locators.
        WebElement groupRoot = resolveElement();

        // Find all <label> or <input> (common radio patterns). This example uses visible text match.
        List<WebElement> options = groupRoot.findElements(By.xpath(".//label[normalize-space()]"));

        boolean found = false;
        for (WebElement label : options) {
            String labelText = label.getText().trim();
            if (labelText.equalsIgnoreCase(valueToSelect)) {
                WebElement radioInput = label.findElement(By.xpath(".//preceding-sibling::input[@type='radio'] | .//input[@type='radio']"));
                DriverActions.click(radioInput);
                found = true;
                System.out.println("RADIO GROUP SELECTED: " + valueToSelect + " for " + command.getFieldKey());
                break;
            }
        }

        if (!found) {
            throw new AssertionError("Radio option not found: value=" + valueToSelect + " field=" + command.getFieldKey());
        }
    }

    @Override
    public void verify(ControlCommand command) {
        String expectedValue = String.valueOf(command.getValue());
        WebElement groupRoot = resolveElement();
        List<WebElement> options = groupRoot.findElements(By.xpath(".//label[normalize-space()]"));

        boolean found = false, selected = false;
        for (WebElement label : options) {
            String labelText = label.getText().trim();
            if (labelText.equalsIgnoreCase(expectedValue)) {
                found = true;
                WebElement radioInput = label.findElement(By.xpath(".//preceding-sibling::input[@type='radio'] | .//input[@type='radio']"));
                selected = radioInput.isSelected();
                break;
            }
        }
        if (!found) throw new AssertionError("Radio option not found: " + expectedValue);
        if (!selected) throw new AssertionError("Radio option not selected: " + expectedValue);

        System.out.println("RADIO GROUP VERIFIED: " + expectedValue + " is selected for " + command.getFieldKey());
    }

    @Override
    public Object read() {
        WebElement groupRoot = resolveElement();
        // Finds the checked radio input inside the group
        WebElement checked = groupRoot.findElement(By.xpath(".//input[@type='radio' and @checked or @type='radio' and @selected or @type='radio' and @aria-checked='true' or @type='radio' and @checked='checked']"));
        if (checked == null) {
            return null;
        }
        // Return the associated label text for the checked radio
        WebElement label = checked.findElement(By.xpath("following-sibling::label | parent::label"));
        return label.getText().trim();
    }
}