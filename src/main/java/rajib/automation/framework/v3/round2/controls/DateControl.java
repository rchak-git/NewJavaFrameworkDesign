package rajib.automation.framework.v3.round2.controls;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import java.util.List;
import java.util.Locale;

public class DateControl extends BaseControl {

    public DateControl(FieldSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }

    private FieldSchema fieldSchema() {
        return (FieldSchema) schema;
    }

    @Override
    public void populate(ControlCommand command) {
        Object valueObj = command.getValue();
        if (valueObj == null) return;

        String value = valueObj.toString().trim();
        String[] parts = value.split(" ");
        int day = Integer.parseInt(parts[0]);
        String monthName = toFullMonthName(parts[1]);
        int year = Integer.parseInt(parts[2]);

        WebElement input = resolver.resolve(fieldSchema());
        input.click();

        WebElement yearSelect = resolver.getDriver().findElement(By.cssSelector(".react-datepicker__year-select"));
        new Select(yearSelect).selectByVisibleText(String.valueOf(year));

        WebElement monthSelect = resolver.getDriver().findElement(By.cssSelector(".react-datepicker__month-select"));
        new Select(monthSelect).selectByVisibleText(monthName);

        String daySuffix = getDaySuffix(day);
        String targetAria = String.format("%s %d%s, %d", monthName, day, daySuffix, year);

        List<WebElement> days = resolver.getDriver().findElements(By.cssSelector(".react-datepicker__day"));

        boolean clicked = false;
        for (WebElement d : days) {
            String ariaLabel = d.getAttribute("aria-label");
            if (ariaLabel != null
                    && ariaLabel.contains(targetAria)
                    && !"true".equals(d.getAttribute("aria-disabled"))) {
                d.click();
                clicked = true;
                break;
            }
        }
        if (!clicked) {
            throw new NoSuchElementException("No clickable calendar day matched: " + targetAria);
        }
    }

    private static String toFullMonthName(String m) {
        m = m.toLowerCase(Locale.ENGLISH);
        switch (m) {
            case "jan": return "January";
            case "feb": return "February";
            case "mar": return "March";
            case "apr": return "April";
            case "may": return "May";
            case "jun": return "June";
            case "jul": return "July";
            case "aug": return "August";
            case "sep": case "sept": return "September";
            case "oct": return "October";
            case "nov": return "November";
            case "dec": return "December";
            case "january": return "January";
            case "february": return "February";
            case "march": return "March";
            case "april": return "April";
            case "june": return "June";
            case "july": return "July";
            case "august": return "August";
            case "september": return "September";
            case "october": return "October";
            case "november": return "November";
            case "december": return "December";
        }
        throw new IllegalArgumentException("Unknown month: " + m);
    }

    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) return "th";
        switch (day % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }

    @Override
    public Object read() {
        return resolver.resolve(fieldSchema()).getAttribute("value");
    }

    @Override
    public void verify(ControlCommand command) {
        Object expectedValue = command.getValue();
        String actualValue = String.valueOf(read());

        if (expectedValue == null && actualValue == null) return;
        if (expectedValue == null || actualValue == null || !expectedValue.toString().equals(actualValue)) {
            throw new AssertionError(
                    "DATE FIELD VERIFY FAILED for field '" + command.getFieldKey() +
                            "' | Expected: " + expectedValue + " | Actual: " + actualValue
            );
        }
        System.out.println("VERIFY PASSED for " + command.getFieldKey());
    }
}