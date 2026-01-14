package rajib.automation.framework.tables.fluent;

import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.tables.actions.TableActionExecutor;

public class TableRowActions {

    private final TableActionExecutor executor;
    private final RowCriteria criteria;

    public TableRowActions(TableActionExecutor executor,
                           RowCriteria criteria) {
        this.executor = executor;
        this.criteria = criteria;
    }


    private int resolveRowIndex() {
        return executor.findSingleMatchingRow(criteria.criteria());
    }


    public void clickCell(String columnName) {

        int rowIndex = resolveRowIndex();

        executor.clickCell(rowIndex, columnName);
    }


    public void clickLink(String columnName) {

        int rowIndex = resolveRowIndex();

        executor.clickInCell(
                rowIndex,
                columnName,
                new LocatorSchema("css", "a")
        );
    }

    public void assertCellEquals(String columnName, String expectedValue) {

        int rowIndex = resolveRowIndex();

        String actualValue =
                executor.getCellValue(rowIndex, columnName);

        if (!expectedValue.equals(actualValue)) {
            throw new AssertionError(
                    "Table assertion failed.\n" +
                            "Row criteria : " + criteria.criteria() + "\n" +
                            "Column      : " + columnName + "\n" +
                            "Expected    : " + expectedValue + "\n" +
                            "Actual      : " + actualValue
            );
        }
    }


    // action methods come in Step 2+
}

