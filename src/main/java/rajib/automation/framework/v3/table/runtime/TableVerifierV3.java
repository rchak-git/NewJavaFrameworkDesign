package rajib.automation.framework.v3.table.runtime;

import org.openqa.selenium.WebElement;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.intent.VerifySpec;
import rajib.automation.framework.tables.actions.TableActionExecutor;
import rajib.automation.framework.v3.table.model.RowVerifySpec;
import rajib.automation.framework.v3.table.model.TableVerificationSpec;
import rajib.automation.framework.v3.verify.VerificationExecutor;
import rajib.automation.framework.codegen.schema.TableColumnSchema;
import rajib.automation.framework.codegen.schema.TableSchema;

import java.util.Map;

public class TableVerifierV3 {

    private final TableActionExecutor executor;
    private final TableSchema tableSchema;

    public TableVerifierV3(
            TableActionExecutor executor,
            TableSchema tableSchema
    ) {
        this.executor = executor;
        this.tableSchema = tableSchema;
    }

    public void verify(TableVerificationSpec spec) {

        if (spec.verify() == null || spec.verify().isEmpty()) {
            throw new IllegalArgumentException(
                    "No verification rules defined for table: " + spec.tableKey()
            );
        }

        for (RowVerifySpec rowSpec : spec.verify()) {

            // 1️⃣ Resolve row
            int rowIndex =
                    executor.findSingleMatchingRow(
                            convertMatchMap(rowSpec.match())
                    );
            // 2️⃣ Apply column verifications
            for (Map.Entry<String, VerifySpec> entry :
                    rowSpec.verify().entrySet()) {

                String columnKey = entry.getKey();
                VerifySpec verifySpec = entry.getValue();

                // Resolve cell element
                WebElement cellElement =
                        executor.resolveCellElement(rowIndex, columnKey);

                // Determine column type (if available)
                FieldType fieldType = resolveFieldType(columnKey);

                String logicalKey =
                        spec.tableKey() +
                                "[match=" + rowSpec.match() + "]." +
                                columnKey;

                VerificationExecutor.verifyElement(
                        logicalKey,
                        fieldType,
                        cellElement,
                        verifySpec
                );
            }
        }
    }

    private FieldType resolveFieldType(String columnKey) {

        for (TableColumnSchema col : tableSchema.columns()) {
            if (col.name().equals(columnKey)) {
                return col.type();   // using existing column FieldType
            }
        }

        // fallback
        return FieldType.ACTION;
    }

    private Map<String, String> convertMatchMap(
            Map<String, Object> rawMatch
    ) {
        Map<String, String> converted = new java.util.LinkedHashMap<>();

        for (Map.Entry<String, Object> e : rawMatch.entrySet()) {
            converted.put(
                    e.getKey(),
                    String.valueOf(e.getValue())
            );
        }

        return converted;
    }
}