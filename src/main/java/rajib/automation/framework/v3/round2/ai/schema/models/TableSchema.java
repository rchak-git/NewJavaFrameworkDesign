package rajib.automation.framework.v3.round2.ai.schema.models;

import java.util.List;

public class TableSchema extends BaseSchema {

    private LocatorSchema tableLocator;
    private LocatorSchema rowLocator;
    private LocatorSchema cellLocator;
    private List<TableColumnSchema> columns;

    // Required by Jackson
    public TableSchema() {}

    public TableSchema(
            String key,
            String logicalName,
            LocatorSchema tableLocator,
            LocatorSchema rowLocator,
            LocatorSchema cellLocator,
            List<TableColumnSchema> columns
    ) {
        super(key, logicalName);
        this.tableLocator = tableLocator;
        this.rowLocator = rowLocator;
        this.cellLocator = cellLocator;
        this.columns = columns;
    }

    public LocatorSchema getTableLocator() { return tableLocator; }
    public void setTableLocator(LocatorSchema tableLocator) { this.tableLocator = tableLocator; }

    public LocatorSchema getRowLocator() { return rowLocator; }
    public void setRowLocator(LocatorSchema rowLocator) { this.rowLocator = rowLocator; }

    public LocatorSchema getCellLocator() { return cellLocator; }
    public void setCellLocator(LocatorSchema cellLocator) { this.cellLocator = cellLocator; }

    public List<TableColumnSchema> getColumns() { return columns; }
    public void setColumns(List<TableColumnSchema> columns) { this.columns = columns; }
}