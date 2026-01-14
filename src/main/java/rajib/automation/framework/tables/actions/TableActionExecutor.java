package rajib.automation.framework.tables.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.codegen.schema.TableColumnSchema;
import rajib.automation.framework.codegen.schema.TableSchema;

import java.util.*;

public class TableActionExecutor {

    private final WebDriver driver;
    private final TableSchema tableSchema;

    public TableActionExecutor(WebDriver driver, TableSchema tableSchema) {
        this.driver = driver;
        this.tableSchema = tableSchema;
    }


    public boolean isTablePresent() {
        return !driver.findElements(toBy(tableSchema.tableLocator())).isEmpty();
    }

    public int getRowCount() {
        WebElement tableRoot =
                driver.findElement(toBy(tableSchema.tableLocator()));
        return tableRoot.findElements(toBy(tableSchema.rowLocator())).size();
    }


    public String getCellValue(int rowIndex, String columnName) {
        int columnIndex = resolveColumnIndex(columnName);
        WebElement row = getRowElement(rowIndex);

        List<WebElement> cells =
                row.findElements(toBy(tableSchema.cellLocator()));

        if (columnIndex >= cells.size()) {
            throw new IndexOutOfBoundsException(
                    "Column index " + columnIndex +
                            " out of bounds for table '" + tableSchema.key() + "'"
            );
        }

        return cells.get(columnIndex).getText().trim();
    }

    public Map<String, String> getRowAsMap(int rowIndex) {
        WebElement row = getRowElement(rowIndex);
        List<WebElement> cells =
                row.findElements(toBy(tableSchema.cellLocator()));

        if (cells.size() < tableSchema.columns().size()) {
            throw new IllegalStateException(
                    "Row cell count does not match column schema for table: " +
                            tableSchema.key()
            );
        }

        Map<String, String> rowData = new LinkedHashMap<>();

        for (int i = 0; i < tableSchema.columns().size(); i++) {
            rowData.put(
                    tableSchema.columns().get(i).name(),
                    cells.get(i).getText().trim()
            );
        }

        return rowData;
    }

    public Optional<Integer> findFirstMatchingRow(
            Map<String, String> criteria) {

        Objects.requireNonNull(criteria, "criteria cannot be null");

        for (int i = 0; i < getRowCount(); i++) {
            Map<String, String> row = getRowAsMap(i);

            boolean match = true;
            for (Map.Entry<String, String> entry : criteria.entrySet()) {
                String column = entry.getKey();
                String expected = entry.getValue();

                if (!row.containsKey(column)) {
                    throw new IllegalArgumentException(
                            "Unknown column in criteria: " + column
                    );
                }

                if (!Objects.equals(row.get(column), expected)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }


    public List<Integer> findAllMatchingRows(
            Map<String, String> criteria) {

        Objects.requireNonNull(criteria, "criteria cannot be null");

        List<Integer> matches = new ArrayList<>();

        for (int i = 0; i < getRowCount(); i++) {
            Map<String, String> row = getRowAsMap(i);

            boolean match = true;
            for (Map.Entry<String, String> entry : criteria.entrySet()) {
                String column = entry.getKey();
                String expected = entry.getValue();

                if (!row.containsKey(column)) {
                    throw new IllegalArgumentException(
                            "Unknown column in criteria: " + column
                    );
                }

                if (!Objects.equals(row.get(column), expected)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                matches.add(i);
            }
        }

        return matches;
    }


    public int findSingleMatchingRow(
            Map<String, String> criteria) {

        List<Integer> matches = findAllMatchingRows(criteria);

        if (matches.isEmpty()) {
            throw new IllegalStateException(
                    "No matching row found for criteria: " + criteria
            );
        }

        if (matches.size() > 1) {
            throw new IllegalStateException(
                    "Multiple matching rows found (" + matches.size() +
                            ") for criteria: " + criteria
            );
        }

        return matches.get(0);
    }


    public void clickCell(int rowIndex, String columnName) {

        int columnIndex = resolveColumnIndex(columnName);

        WebElement row = getRowElement(rowIndex);

        List<WebElement> cells =
                row.findElements(toBy(tableSchema.cellLocator()));

        if (columnIndex < 0 || columnIndex >= cells.size()) {
            throw new IndexOutOfBoundsException(
                    "Column index " + columnIndex +
                            " out of bounds for table '" + tableSchema.key() +
                            "'. Total cells: " + cells.size()
            );
        }

        WebElement cell = cells.get(columnIndex);
        cell.click();
    }


    public void clickInCell(int rowIndex,
                            String columnName,
                            LocatorSchema childLocator) {
        int columnIndex = resolveColumnIndex(columnName);

        WebElement row = getRowElement(rowIndex);

        List<WebElement> cells =
                row.findElements(toBy(tableSchema.cellLocator()));

        if (columnIndex < 0 || columnIndex >= cells.size()) {
            throw new IndexOutOfBoundsException(
                    "Column index " + columnIndex +
                            " out of bounds for table '" + tableSchema.key() +
                            "'. Total cells: " + cells.size()
            );
        }

        WebElement cell = cells.get(columnIndex);
        WebElement child;

        try {
            child = cell.findElement(toBy(childLocator));
        } catch (NoSuchElementException e) {
            throw new IllegalStateException(
                    "Child element not found in cell for table '" + tableSchema.key() +
                            "', column '" + columnName +
                            "', locator: " + childLocator,
                    e
            );
        }

        child.click();
    }







    // reuse locator conversion (same as TableReader)
    private By toBy(LocatorSchema locator) {
        return switch (locator.strategy().toLowerCase()) {
            case "id" -> By.id(locator.value());
            case "css", "cssselector" -> By.cssSelector(locator.value());
            case "xpath" -> By.xpath(locator.value());
            default -> throw new IllegalArgumentException(
                    "Unsupported locator strategy: " + locator.strategy()
            );
        };
    }

    private int resolveColumnIndex(String columnName) {

        if (tableSchema.columns() == null || tableSchema.columns().isEmpty()) {
            throw new IllegalStateException(
                    "No column schema defined for table: " + tableSchema.key()
            );
        }

        for (int i = 0; i < tableSchema.columns().size(); i++) {
            TableColumnSchema col = tableSchema.columns().get(i);
            if (col.name().equalsIgnoreCase(columnName)) {
                return i;
            }
        }

        throw new IllegalArgumentException(
                "Unknown column '" + columnName + "' for table: " + tableSchema.key()
        );
    }


    private WebElement getRowElement(int rowIndex) {

        WebElement tableRoot =
                driver.findElement(toBy(tableSchema.tableLocator()));

        List<WebElement> rows =
                tableRoot.findElements(toBy(tableSchema.rowLocator()));

        if (rowIndex < 0 || rowIndex >= rows.size()) {
            throw new IndexOutOfBoundsException(
                    "Row index " + rowIndex +
                            " out of bounds for table '" + tableSchema.key() +
                            "'. Total rows: " + rows.size()
            );
        }

        return rows.get(rowIndex);
    }

}
