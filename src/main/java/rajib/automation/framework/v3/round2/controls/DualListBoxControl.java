/*
package rajib.automation.framework.v3.round2.controls;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
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

        // Fix: Find options, not li's
        List<WebElement> allOptions = srcList.findElements(By.tagName("option"));
        for (WebElement option : allOptions) {
            System.out.println("Found option: " + option.getText());
        }

        for (String val : desiredValues) {
            // Fix: Look for option, not li
            WebElement option = srcList.findElement(
                    By.xpath(".//option[normalize-space(.)='" + val + "']")
            );
            option.click();
            addBtn.click();
        }

        clearAllSelections(resolver.resolve(schema, "target"));
    }
    private void removeItems(ControlCommand command) {
        @SuppressWarnings("unchecked")
        List<String> values = (List<String>) command.getValue();
        if (values == null) return;

        WebElement removeBtn = getElement("removeButton");

        for (String val : values) {
            // Always re-fetch the target list to avoid stale DOM issues
            WebElement tgtList = getElement("target");
            try {
                WebElement option = tgtList.findElement(By.xpath(".//option[normalize-space(.)='" + val + "']"));
                option.click();
                removeBtn.click();

                // Wait for option to disappear from target (basic approach)
                boolean removed = false;
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100); // replace with an actual wait in production!
                    List<WebElement> stillPresent = tgtList.findElements(By.xpath(".//option[normalize-space(.)='" + val + "']"));
                    if (stillPresent.isEmpty()) {
                        removed = true;
                        break;
                    }
                }
                if (!removed) {
                    System.out.println("Warning: Option '" + val + "' was not removed from target in expected time.");
                }
            } catch (org.openqa.selenium.NoSuchElementException e) {
                System.out.println("Option '" + val + "' not present in target list.");
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("Target list has changed, retrying lookup...");
                // Optionally retry (usually handled above by re-fetching tgtList).
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
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

    private void clearAllSelections(WebElement selectElement) {
        if (selectElement.getTagName().equalsIgnoreCase("select")) {
            Select select = new Select(selectElement);
            select.deselectAll();
        } else {
            // fallback for non-select multi-controls, if you have them
            List<WebElement> selectedOptions = selectElement.findElements(By.cssSelector(".selected"));
            for (WebElement option : selectedOptions) {
                option.click();
            }
        }
    }
}

 */