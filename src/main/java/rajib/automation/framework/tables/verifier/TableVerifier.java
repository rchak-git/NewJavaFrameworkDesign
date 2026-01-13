package rajib.automation.framework.tables.verifier;

import rajib.automation.framework.tables.reader.TableReader;

import java.util.Objects;

/**
 * WT-2: Read-only assertion layer for tables.
 *
 * Responsibilities:
 * - Perform assertions using TableReader
 * - NO WebDriver usage
 * - NO DOM access
 * - NO mutations
 */
public class TableVerifier {

    private final TableReader reader;

    public TableVerifier(TableReader reader) {
        this.reader = Objects.requireNonNull(reader, "TableReader cannot be null");
    }

    // --------------------
    // Table-level checks
    // --------------------

    public TableVerifier hasAtLeastRows(int expectedMinRows) {
        int actualRows = reader.getRowCount();
        if (actualRows < expectedMinRows) {
            throw new AssertionError(
                    "Expected at least " + expectedMinRows + " rows, but found " + actualRows
            );
        }
        return this;
    }

    public TableVerifier hasExactlyRows(int expectedRows) {
        int actualRows = reader.getRowCount();
        if (actualRows != expectedRows) {
            throw new AssertionError(
                    "Expected exactly " + expectedRows + " rows, but found " + actualRows
            );
        }
        return this;
    }

    // --------------------
    // Cell-level checks
    // --------------------

    public TableVerifier cellEquals(int rowIndex, String columnName, String expectedValue) {
        String actual = reader.getCellText(rowIndex, columnName);
        if (!Objects.equals(actual, expectedValue)) {
            throw new AssertionError(
                    "Cell mismatch at row " + rowIndex + ", column '" + columnName
                            + "'. Expected: [" + expectedValue + "], Actual: [" + actual + "]"
            );
        }
        return this;
    }

    public TableVerifier cellContains(int rowIndex, String columnName, String expectedSubstring) {
        String actual = reader.getCellText(rowIndex, columnName);
        if (actual == null || !actual.contains(expectedSubstring)) {
            throw new AssertionError(
                    "Cell text does not contain expected value at row "
                            + rowIndex + ", column '" + columnName
                            + "'. Expected to contain: [" + expectedSubstring
                            + "], Actual: [" + actual + "]"
            );
        }
        return this;
    }
}
