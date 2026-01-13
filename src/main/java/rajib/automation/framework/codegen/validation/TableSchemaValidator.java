package rajib.automation.framework.codegen.validation;



import rajib.automation.framework.codegen.schema.TableSchema;

import java.util.List;

public final class TableSchemaValidator {

    private TableSchemaValidator() {
        // utility class
    }

    /**
     * Validates table schemas structurally.
     * WT-1.3 only: no DOM access, no Selenium.
     */
    public static void validate(List<TableSchema> tables, String pageName) {

        if (tables == null || tables.isEmpty()) {
            return; // pages may legitimately have no tables
        }

        for (TableSchema table : tables) {

            if (table.key() == null || table.key().isBlank()) {
                throw new IllegalArgumentException(
                        "TableSchema key cannot be null/blank on page: " + pageName
                );
            }

            if (table.tableLocator() == null) {
                throw new IllegalArgumentException(
                        "Missing tableLocator for table '" + table.key()
                                + "' on page: " + pageName
                );
            }

            if (table.rowLocator() == null) {
                throw new IllegalArgumentException(
                        "Missing rowLocator for table '" + table.key()
                                + "' on page: " + pageName
                );
            }

            if (table.cellLocator() == null) {
                throw new IllegalArgumentException(
                        "Missing cellLocator for table '" + table.key()
                                + "' on page: " + pageName
                );
            }
        }
    }
}
