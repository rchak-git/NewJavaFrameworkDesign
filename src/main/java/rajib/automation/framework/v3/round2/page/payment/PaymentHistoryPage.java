package rajib.automation.framework.v3.round2.page.payment;

import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.TableColumnSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.TableSchema;
import rajib.automation.framework.v3.round2.controls.TableControl;
import rajib.automation.framework.v3.round2.control.Control;
import rajib.automation.framework.v3.round2.page.BasePageR2;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Page class generated from PaymentHistoryPageSchema.json
 *
 * Notes / gaps:
 * - The schema declares a field with fieldType = STATIC_TEXT (key = "pageTitle").
 *   There is no concrete control implementation for STATIC_TEXT in the codebase
 *   (e.g., no StaticTextControl or LabelControl). Per contract we must NOT guess
 *   the correct control mapping. Therefore this class preserves the field schema
 *   and exposes a member for the control, but does NOT instantiate or register it.
 *
 *   To complete this page you can either:
 *     - Implement a concrete control class (e.g., StaticTextControl extends BaseControl),
 *       then instantiate and register it here (registerControl("pageTitle", pageTitle);)
 *     - Or instruct me which existing control type should be used for STATIC_TEXT
 *       and I will update this page to instantiate/register that control.
 */
public class PaymentHistoryPage extends BasePageR2 {

    // Field: pageTitle (STATIC_TEXT) — schema preserved. No concrete control exists in project for STATIC_TEXT.
    private Control pageTitle; // left uninitialized because concrete control class is missing

    // Table: paymentsTable
    private final TableControl paymentsTable;

    // Table metadata derived from schema (kept as part of page class for use by tests)
    private final String paymentsTableIdentifierColumnKey;
    private final String paymentsTableRowSelector;
    private final Map<String, Integer> paymentsTableColumnIndexMap = new HashMap<>();

    public PaymentHistoryPage(ElementResolver resolver, RuntimeContext runtimeContext) {
        super(resolver, runtimeContext);

        // Build FieldSchema for pageTitle (STATIC_TEXT)
        FieldSchema pageTitleSchema = buildFieldSchema(
                "pageTitle",
                FieldType.STATIC_TEXT,
                "Payment History Title",
                "main",
                "css",
                "h2"
        );

        // NOTE: No concrete control class exists for STATIC_TEXT in the codebase.
        // Do NOT instantiate a control here. Once a StaticTextControl (or similar)
        // is added to the project, instantiate it here and register it:
        //
        //     pageTitle = new StaticTextControl(pageTitleSchema, resolver);
        //     registerControl("pageTitle", pageTitle);
        //
        // For now we keep the schema available (pageTitleSchema) but do not register.

        // Populate the table metadata (columns, identifier column) as per the schema file
        // from PaymentHistoryPageSchema.json:
        //
        // columns:
        //   paymentId (index 1)
        //   customerName (index 2)
        //   amount (index 3)
        //   scenario (index 4)
        //   status (index 5)
        //   createdAt (index 6)
        //
        // metadata:
        //   identifierColumn: paymentId
        //   rowSelector: tbody tr

        paymentsTableColumnIndexMap.put("paymentId", 1);
        paymentsTableColumnIndexMap.put("customerName", 2);
        paymentsTableColumnIndexMap.put("amount", 3);
        paymentsTableColumnIndexMap.put("scenario", 4);
        paymentsTableColumnIndexMap.put("status", 5);
        paymentsTableColumnIndexMap.put("createdAt", 6);

        paymentsTableIdentifierColumnKey = "paymentId";
        paymentsTableRowSelector = "tbody tr";

        // Build table schema for paymentsTable and attach columns
        TableSchema paymentsTableSchema = buildTableSchema(
                "paymentsTable",
                "Payments Table",
                "css",
                "table",
                "tbody tr",
                "td",
                List.of(
                        new TableColumnSchema("paymentId", 1, FieldType.STATIC_TEXT),
                        new TableColumnSchema("customerName", 2, FieldType.STATIC_TEXT),
                        new TableColumnSchema("amount", 3, FieldType.STATIC_TEXT),
                        new TableColumnSchema("scenario", 4, FieldType.STATIC_TEXT),
                        new TableColumnSchema("status", 5, FieldType.STATIC_TEXT),
                        new TableColumnSchema("createdAt", 6, FieldType.STATIC_TEXT)
                )
        );

        // Instantiate table control and register it
        paymentsTable = new TableControl(paymentsTableSchema, resolver);
        registerControl("paymentsTable", paymentsTable);
    }

    /**
     * Expose table metadata helpers for test code / verification logic.
     */
    public TableControl getPaymentsTable() {
        return paymentsTable;
    }

    public String getPaymentsTableIdentifierColumnKey() {
        return paymentsTableIdentifierColumnKey;
    }

    public String getPaymentsTableRowSelector() {
        return paymentsTableRowSelector;
    }

    /**
     * Returns the 1-based column index for a given column key (as declared in the schema).
     * If the key is unknown, returns -1.
     */
    public int getPaymentsTableColumnIndex(String columnKey) {
        return paymentsTableColumnIndexMap.getOrDefault(columnKey, -1);
    }

    /**
     * Helper builders (same pattern used in other pages in the codebase)
     */
    private FieldSchema buildFieldSchema(
            String key,
            FieldType fieldType,
            String logicalName,
            String locatorKey,
            String strategy,
            String value
    ) {
        LocatorSchema locatorSchema = new LocatorSchema();
        locatorSchema.setStrategy(strategy);
        locatorSchema.setValue(value);

        FieldSchema fieldSchema = new FieldSchema();
        fieldSchema.setKey(key);
        fieldSchema.setFieldType(fieldType);
        fieldSchema.setLogicalName(logicalName);
        fieldSchema.setLocators(Map.of(locatorKey, locatorSchema));

        return fieldSchema;
    }

    private TableSchema buildTableSchema(
            String key,
            String logicalName,
            String strategy,
            String tableValue,
            String rowValue,
            String cellValue,
            List<TableColumnSchema> columns
    ) {
        LocatorSchema tableLocator = new LocatorSchema();
        tableLocator.setStrategy(strategy);
        tableLocator.setValue(tableValue);

        LocatorSchema rowLocator = new LocatorSchema();
        rowLocator.setStrategy(strategy);
        rowLocator.setValue(rowValue);

        LocatorSchema cellLocator = new LocatorSchema();
        cellLocator.setStrategy(strategy);
        cellLocator.setValue(cellValue);

        TableSchema tableSchema = new TableSchema();
        tableSchema.setKey(key);
        tableSchema.setLogicalName(logicalName);
        tableSchema.setTableLocator(tableLocator);
        tableSchema.setRowLocator(rowLocator);
        tableSchema.setCellLocator(cellLocator);
        tableSchema.setColumns(columns);

        return tableSchema;
    }
}