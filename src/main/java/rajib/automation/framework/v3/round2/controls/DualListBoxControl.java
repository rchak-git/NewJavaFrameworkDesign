package rajib.automation.framework.v3.round2.controls;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.Control;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;


import java.util.ArrayList;
import java.util.List;

public class DualListBoxControl extends BaseControl implements Control {

    public DualListBoxControl(FieldSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }


    @Override
    public void doAction(ControlCommand command) {
        switch (command.getAction()) {
            case ADD -> addItems(command);
            case REMOVE -> removeItems(command);
            case ADD_ALL -> addAll();
            case REMOVE_ALL -> removeAll();
            default -> throw new UnsupportedOperationException(
                    "Unknown action: " + command.getAction());
        }
    }
    // Helper for locator lookup by key
    private WebElement getElement(String locatorKey) {
        return resolver.resolve(schema, locatorKey);
    }

    @Override
    public void populate(ControlCommand command) {
        // Determine the action: default is "add"
        String action = (command.getAction() == null) ? "add" : command.getAction().name().toLowerCase();


        switch (action) {
            case "add":
                addItems(command);
                break;
            case "remove":
                removeItems(command);
                break;
            case "addAll":
                addAll();
                break;
            case "removeAll":
                removeAll();
                break;
            default:
                throw new UnsupportedOperationException("Unknown DualListBox action: " + action);
        }

        System.out.println("POPULATE DUALLISTBOX " + command.getFieldKey() + " action='" + action + "' value=" + command.getValue());
    }

    private void addItems(ControlCommand command) {
        @SuppressWarnings("unchecked")
        List<String> desiredValues = (List<String>) command.getValue();
        if (desiredValues == null) return;
        WebElement srcList = getElement("source");
        WebElement addBtn = getElement("addButton");
        List<WebElement> allLis = srcList.findElements(By.tagName("li"));
        for (WebElement li : allLis) {
            System.out.println("Found LI: " + li.getText());
        }

        for (String val : desiredValues) {
            WebElement option = srcList.findElement(By.xpath(".//li[normalize-space(.)='" + val + "']"));
            option.click();
            addBtn.click();
        }

        clearAllSelections(resolver.resolve(schema, "target"));
    }

    private void removeItems(ControlCommand command) {
        @SuppressWarnings("unchecked")
        List<String> values = (List<String>) command.getValue();
        if (values == null) return;
        WebElement tgtList = getElement("target");
        WebElement removeBtn = getElement("removeButton");
        for (String val : values) {
            WebElement option = tgtList.findElement(By.xpath(".//li[normalize-space(.)='" + val + "']"));
            option.click();
            removeBtn.click();
        }
        clearAllSelections(resolver.resolve(schema, "source"));

    }

    private void addAll() {
        WebElement addAllBtn = getElement("addAllButton");
        addAllBtn.click();
    }

    private void removeAll() {
        WebElement removeAllBtn = getElement("removeAllButton");
        removeAllBtn.click();
    }

    @Override
    public void verify(ControlCommand command) {
        @SuppressWarnings("unchecked")
        List<String> expectedValues = (List<String>) command.getValue();
        List<String> actualValues = (List<String>) read();

        if (!actualValues.containsAll(expectedValues)) {
            throw new AssertionError(
                    "LIST_CONTAINS_ALL failed for field '" + command.getFieldKey() +
                            "' | Expected at least: " + expectedValues + " | Actual: " + actualValues
            );
        }
        System.out.println("VERIFY PASSED for " + command.getFieldKey());
    }

    @Override
    public Object read() {
        // Return all option texts from right/target list
        WebElement targetList = getElement("target");
        List<WebElement> options = targetList.findElements(By.tagName("option"));
        List<String> values = new ArrayList<>();
        for (WebElement option : options) {
            values.add(option.getText().trim());
        }
        return values;
    }

    private void clearAllSelections(WebElement ul) {
        List<WebElement> selectedLis = ul.findElements(By.cssSelector("li.list-group-item.active"));
        for (WebElement li : selectedLis) {
            li.click(); // toggling de-selects
        }
    }
}