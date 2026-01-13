package rajib.automation.framework.model.table;

import org.openqa.selenium.By;
import java.util.List;

public class PageTable {
    private final String tableName;
    private final By tableLocator;
    private final By rowLocator;      // optional override
    private final By cellLocator;     // optional override
    private final List<TableColumn> columns;

    public PageTable(String tableName, By tableLocator, List<TableColumn> columns) {
        this(tableName, tableLocator, By.cssSelector("tbody tr"), By.cssSelector("td"), columns);
    }

    public PageTable(String tableName, By tableLocator, By rowLocator, By cellLocator, List<TableColumn> columns) {
        this.tableName = tableName;
        this.tableLocator = tableLocator;
        this.rowLocator = rowLocator;
        this.cellLocator = cellLocator;
        this.columns = columns;
    }

    public String getTableName() { return tableName; }
    public By getTableLocator() { return tableLocator; }
    public By getRowLocator() { return rowLocator; }
    public By getCellLocator() { return cellLocator; }
    public List<TableColumn> getColumns() { return columns; }

    public TableColumn getColumn(String name) {
        return columns.stream()
                .filter(c -> c.getColumnName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown column '" + name + "' for table: " + tableName));
    }
}

