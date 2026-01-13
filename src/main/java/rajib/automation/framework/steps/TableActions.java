package rajib.automation.framework.steps;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import rajib.automation.framework.model.table.PageTable;
import rajib.automation.framework.model.table.TableColumn;

import java.util.List;

public class TableActions {

    private final WebDriver driver;

    public TableActions(WebDriver driver) {
        this.driver = driver;
    }

    private WebElement tableRoot(PageTable table) {
        return driver.findElement(table.getTableLocator());
    }

    private List<WebElement> rows(PageTable table) {
        return tableRoot(table).findElements(table.getRowLocator());
    }

    private List<WebElement> cells(WebElement row, PageTable table) {
        return row.findElements(table.getCellLocator());
    }

    private WebElement findRow(PageTable table, String matchColumn, String matchValue) {
        TableColumn col = table.getColumn(matchColumn);
        int idx = col.getColumnIndex();

        for (WebElement row : rows(table)) {
            List<WebElement> rowCells = cells(row, table);
            if (idx >= rowCells.size()) continue;

            String cellText = rowCells.get(idx).getText().trim();
            if (cellText.equalsIgnoreCase(matchValue.trim())) {
                return row;
            }
        }

        throw new NoSuchElementException(
                "Row not found in table '" + table.getTableName() + "' where " +
                        matchColumn + " = '" + matchValue + "'");
    }



    public String getCellValue(PageTable table,
                               String matchColumn, String matchValue,
                               String targetColumn) {

        WebElement row = findRow(table, matchColumn, matchValue);
        TableColumn target = table.getColumn(targetColumn);
        List<WebElement> rowCells = cells(row, table);

        int idx = target.getColumnIndex();
        if (idx >= rowCells.size()) {
            throw new IllegalStateException("Column index out of bounds for '" + targetColumn + "'");
        }

        WebElement cell = rowCells.get(idx);
        return cell.getText().trim();
    }


    public void clickInRow(PageTable table,
                           String matchColumn, String matchValue,
                           String targetColumn) {

        WebElement row = findRow(table, matchColumn, matchValue);
        TableColumn target = table.getColumn(targetColumn);

        WebElement cell = cells(row, table).get(target.getColumnIndex());

        switch (target.getType()) {
            case BUTTON:
                cell.findElement(By.tagName("button")).click();
                break;
            case LINK:
                cell.findElement(By.tagName("a")).click();
                break;
            default:
                throw new IllegalArgumentException(
                        "clickInRow() requires BUTTON or LINK column type. Found: " + target.getType());
        }
    }

    public void setCellValue(PageTable table,
                             String matchColumn, String matchValue,
                             String targetColumn,
                             String value) {

        WebElement row = findRow(table, matchColumn, matchValue);
        TableColumn target = table.getColumn(targetColumn);

        WebElement cell = cells(row, table).get(target.getColumnIndex());

        switch (target.getType()) {
            case TEXTBOX: {
                WebElement input = cell.findElement(By.cssSelector("input,textarea"));
                input.clear();
                input.sendKeys(value);
                break;
            }
            case DROPDOWN: {
                WebElement selectEl = cell.findElement(By.tagName("select"));
                new Select(selectEl).selectByVisibleText(value);
                break;
            }
            case CHECKBOX: {
                WebElement checkbox = cell.findElement(By.cssSelector("input[type='checkbox']"));
                boolean desired = Boolean.parseBoolean(value);
                if (checkbox.isSelected() != desired) {
                    checkbox.click();
                }
                break;
            }
            default:
                throw new IllegalArgumentException(
                        "setCellValue() supports TEXTBOX/DROPDOWN/CHECKBOX. Found: " + target.getType());
        }
    }

    public boolean isRowPresent(PageTable table, String matchColumn, String matchValue) {
        try {
            findRow(table, matchColumn, matchValue);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }




}
