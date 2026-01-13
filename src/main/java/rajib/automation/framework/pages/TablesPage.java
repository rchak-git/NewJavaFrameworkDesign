package rajib.automation.framework.pages;

import org.openqa.selenium.By;
import rajib.automation.framework.model.table.ColumnType;
import rajib.automation.framework.model.table.PageTable;
import rajib.automation.framework.model.table.TableColumn;

import java.util.List;

public class TablesPage {

    public final PageTable usersTable;

    public TablesPage() {

        usersTable = new PageTable(
                "Users Table",
                By.id("table1"),
                List.of(
                        new TableColumn("Last Name", 0, ColumnType.TEXT),
                        new TableColumn("First Name", 1, ColumnType.TEXT),
                        new TableColumn("Email", 2, ColumnType.TEXT),
                        new TableColumn("Due", 3, ColumnType.TEXT),
                        new TableColumn("Web Site", 4, ColumnType.LINK),
                        new TableColumn("Action", 5, ColumnType.LINK)
                )
        );
    }
}
