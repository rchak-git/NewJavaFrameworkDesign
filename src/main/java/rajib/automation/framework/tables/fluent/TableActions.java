package rajib.automation.framework.tables.fluent;

import rajib.automation.framework.tables.actions.TableActionExecutor;

public class TableActions {

    private final TableActionExecutor executor;

    public TableActions(TableActionExecutor executor) {
        this.executor = executor;
    }

    public TableRowActions row(RowCriteria criteria) {
        return new TableRowActions(executor, criteria);
    }
}

