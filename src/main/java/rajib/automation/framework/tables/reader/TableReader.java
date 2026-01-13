package rajib.automation.framework.tables.reader;


import org.openqa.selenium.By;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.codegen.schema.TableColumnSchema;
import rajib.automation.framework.codegen.schema.TableSchema;
import org.openqa.selenium.WebDriver;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * WT-2 CONTRACT:
 * - Read-only access to table data
 * - No clicks, no mutations, no assertions
 * - Uses TableSchema exclusively for structure
 *
 * Future actions belong in:
 * - TableActionExecutor (WT-3)
 * - TableVerifier (already separated)
 */

public class TableReader {

    protected final WebDriver driver;
    protected final TableSchema tableSchema;

    public TableReader(WebDriver driver, TableSchema tableSchema) {
        this.driver = driver;
        this.tableSchema = tableSchema;
    }

    // ---- Row / Column info ----

    public int getRowCount() {
        return driver
                .findElement(toBy(tableSchema.tableLocator()))
                .findElements(toBy(tableSchema.rowLocator()))
                .size();
    }


    public int getColumnCount() {

        int rowCount = getRowCount();
        if (rowCount == 0) {
            return 0;
        }

        return driver
                .findElement(toBy(tableSchema.tableLocator()))
                .findElements(toBy(tableSchema.rowLocator()))
                .get(0)
                .findElements(toBy(tableSchema.cellLocator()))
                .size();
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


    public boolean hasHeader(String headerName) {
        throw new UnsupportedOperationException("WT-2 Step-1: Not implemented yet");
    }

    // ---- Cell reading ----

    public String getCellText(int rowIndex, int columnIndex) {

        return driver
                .findElement(toBy(tableSchema.tableLocator()))
                .findElements(toBy(tableSchema.rowLocator()))
                .get(rowIndex)
                .findElements(toBy(tableSchema.cellLocator()))
                .get(columnIndex)
                .getText();
    }


    public String getCellText(int rowIndex, String columnName) {

        int columnIndex = resolveColumnIndex(columnName);
        return getCellText(rowIndex, columnIndex);
    }


    public Map<String, String> getRowAsMap(int rowIndex) {
        Map<String, String> row = new LinkedHashMap<>();

        for (TableColumnSchema col : tableSchema.columns()) {
            String value = getCellText(rowIndex, col.name());
            row.put(col.name(), value);
        }

        return row;
    }

   private By toBy(LocatorSchema locator) {
        return switch (locator.strategy().toLowerCase()) {
            case "id" -> By.id(locator.value());
            case "name" -> By.name(locator.value());
            case "css", "cssselector" -> By.cssSelector(locator.value());
            case "xpath" -> By.xpath(locator.value());
            case "classname" -> By.className(locator.value());
            case "tagname" -> By.tagName(locator.value());
            case "linktext" -> By.linkText(locator.value());
            case "partiallinktext" -> By.partialLinkText(locator.value());
            default -> throw new IllegalArgumentException(
                    "Unsupported locator strategy: " + locator.strategy()
            );
        };
    }


    // return all rows of the table as a list of maps keyed by column name
    public java.util.List<Map<String, String>> getAllRows() {
        java.util.List<Map<String, String>> allRows = new java.util.ArrayList<>();
        int rowCount = getRowCount();
        for (int i = 0; i < rowCount; i++) {
            allRows.add(getRowAsMap(i));
        }
        return allRows;
    }



}
