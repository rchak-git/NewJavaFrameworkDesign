package rajib.automation.framework.tables.verifier;

import rajib.automation.framework.tables.actions.TableActionExecutor;

import java.util.Map;

public class TableJsonVerifier {

    private final TableActionExecutor executor;

    public TableJsonVerifier(TableActionExecutor executor) {
        this.executor = executor;
    }

    public void verify(TableVerificationSpec spec) {

        if (spec.verify() == null || spec.verify().isEmpty()) {
            throw new IllegalArgumentException(
                    "No verification rules defined for table: " + spec.tableKey()
            );
        }

        for (RowVerificationSpec rowSpec : spec.verify()) {

            // --- Step 1: Resolve row using WT-4.2 ---
            int rowIndex = executor.findSingleMatchingRow(rowSpec.match());

            // --- Step 2: Read row data ---
            Map<String, String> actualRow =
                    executor.getRowAsMap(rowIndex);

            // --- Step 3: Apply assertions ---
            for (Map.Entry<String, AssertionSpec> assertionEntry
                    : rowSpec.assertThat().entrySet()) {

                String column = assertionEntry.getKey();
                AssertionSpec assertion = assertionEntry.getValue();

                if (!actualRow.containsKey(column)) {
                    throw new AssertionError(
                            "Table '" + spec.tableKey() + "' verification failed.\n" +
                                    "Column not found: " + column + "\n" +
                                    "Row match criteria: " + rowSpec.match()
                    );
                }

                String actualValue = actualRow.get(column);
                String expectedValue = assertion.value();

                switch (assertion.effectiveType()) {

                    case "EQUALS" -> {
                        if (!expectedValue.equals(actualValue)) {
                            throw new AssertionError(
                                    "Table '" + spec.tableKey() + "' verification failed.\n" +
                                            "Row match criteria: " + rowSpec.match() + "\n" +
                                            "Column: " + column + "\n" +
                                            "Expected: " + expectedValue + "\n" +
                                            "Actual  : " + actualValue
                            );
                        }
                    }

                    default -> throw new IllegalArgumentException(
                            "Unsupported assertion type: " + assertion.effectiveType()
                    );
                }
            }
        }
    }
}
