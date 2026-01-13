package rajib.automation.framework.model.table;

public class TableColumn {
    private final String columnName;
    private final int columnIndex;
    private final ColumnType type;

    public TableColumn(String columnName, int columnIndex, ColumnType type) {
        this.columnName = columnName;
        this.columnIndex = columnIndex;
        this.type = type;
    }

    public String getColumnName() { return columnName; }
    public int getColumnIndex() { return columnIndex; }
    public ColumnType getType() { return type; }
}
