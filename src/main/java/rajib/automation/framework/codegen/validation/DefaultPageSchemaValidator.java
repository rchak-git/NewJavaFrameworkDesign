package rajib.automation.framework.codegen.validation;

import rajib.automation.framework.codegen.schema.PageSchema;

import rajib.automation.framework.codegen.schema.*;
import rajib.automation.framework.enums.CompositeType;
import rajib.automation.framework.enums.FieldType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultPageSchemaValidator implements PageSchemaValidator {

    private static final String SUPPORTED_FRAMEWORK = "BasePage_PageField";

    @Override
    public void validate(PageSchema schema) {
        validateRoot(schema);
        validateFramework(schema.framework());
        validateAtomicFields(schema.fields());
        validateCompositeFields(schema.compositeFields());
        validateNoDuplicateKeys(schema);
    }


    private void validateTables(List<TableSchema> tables) {

        if (tables == null) {
            return; // tables are optional
        }

        for (TableSchema table : tables) {
            validateTable(table);
        }
    }

    private void validateTable(TableSchema table) {

        if (table == null) {
            throw new SchemaValidationException("TableSchema must not be null");
        }

        if (isBlank(table.key())) {
            throw new SchemaValidationException("Table key must not be blank");
        }

        validateLocator(table.tableLocator(), "tableLocator", table.key());
        validateLocator(table.rowLocator(), "rowLocator", table.key());
        validateLocator(table.cellLocator(), "cellLocator", table.key());

        if (table.columns() == null || table.columns().isEmpty()) {
            throw new SchemaValidationException(
                    "Table '" + table.key() + "' must define at least one column"
            );
        }

        validateTableColumns(table);
    }


    private void validateTableColumns(TableSchema table) {

        Set<String> columnNames = new HashSet<>();

        for (TableColumnSchema column : table.columns()) {

            if (column == null) {
                throw new SchemaValidationException(
                        "Null column found in table: " + table.key()
                );
            }

            if (isBlank(column.name())) {
                throw new SchemaValidationException(
                        "Column name must not be blank in table: " + table.key()
                );
            }

            if (!columnNames.add(column.name())) {
                throw new SchemaValidationException(
                        "Duplicate column name '" + column.name()
                                + "' in table: " + table.key()
                );
            }

            if (column.index() < 0) {
                throw new SchemaValidationException(
                        "Column index must be >= 0 for column '"
                                + column.name() + "' in table: " + table.key()
                );
            }

            if (column.type() == null) {
                throw new SchemaValidationException(
                        "Column type missing for column '"
                                + column.name() + "' in table: " + table.key()
                );
            }
        }
    }

    // ---------- Root ----------
    private void validateRoot(PageSchema schema) {
        if (schema == null) {
            throw new SchemaValidationException("PageSchema must not be null");
        }
        if (isBlank(schema.pageName())) {
            throw new SchemaValidationException("pageName must be provided");
        }
        if (schema.fields() == null) {
            throw new SchemaValidationException("fields must not be null");
        }
        if (schema.compositeFields() == null) {
            throw new SchemaValidationException("compositeFields must not be null");
        }
    }

    // ---------- Framework ----------
    private void validateFramework(String framework) {
        if (!SUPPORTED_FRAMEWORK.equals(framework)) {
            throw new SchemaValidationException(
                    "Unsupported framework: " + framework
            );
        }
    }

    // ---------- Atomic Fields ----------
    private void validateAtomicFields(List<FieldSchema> fields) {
        for (FieldSchema field : fields) {
            validateField(field);
        }
    }

    private void validateField(FieldSchema field) {
        if (field == null) {
            throw new SchemaValidationException("FieldSchema must not be null");
        }
        if (isBlank(field.key())) {
            throw new SchemaValidationException("Field key must not be blank");
        }
        if (field.fieldType() == null) {
            throw new SchemaValidationException(
                    "FieldType missing for field: " + field.key()
            );
        }
        if (field.locator() == null) {
            throw new SchemaValidationException(
                    "Locator missing for field: " + field.key()
            );
        }
        validateLocator(field.locator(), field.key());
    }

    // ---------- Composite Fields ----------
    private void validateCompositeFields(List<CompositeFieldSchema> composites) {
        for (CompositeFieldSchema composite : composites) {
            validateComposite(composite);
        }
    }

    private void validateComposite(CompositeFieldSchema composite) {
        if (composite == null) {
            throw new SchemaValidationException("CompositeFieldSchema must not be null");
        }
        if (isBlank(composite.key())) {
            throw new SchemaValidationException("Composite key must not be blank");
        }
        if (composite.compositeType() == null) {
            throw new SchemaValidationException(
                    "CompositeType missing for composite: " + composite.key()
            );
        }
        if (composite.components() == null || composite.components().isEmpty()) {
            throw new SchemaValidationException(
                    "Composite must have components: " + composite.key()
            );
        }

        // Current rule: COMPOSITE_DROPDOWN → all components must be DROPDOWN
        if (composite.compositeType() == CompositeType.COMPOSITE_DROPDOWN) {
            for (FieldSchema component : composite.components()) {
                if (component.fieldType() != FieldType.DROPDOWN) {
                    throw new SchemaValidationException(
                            "Component must be DROPDOWN in composite: " + composite.key()
                    );
                }
                validateField(component);
            }
        }
    }

    // ---------- Locator ----------
    private void validateLocator(LocatorSchema locator, String fieldKey) {
        if (locator.strategy() == null) {
            throw new SchemaValidationException(
                    "Locator strategy missing for field: " + fieldKey
            );
        }
        if (isBlank(locator.value())) {
            throw new SchemaValidationException(
                    "Locator value missing for field: " + fieldKey
            );
        }
    }

    // 🔹 Overload specifically for table locators
    private void validateLocator(LocatorSchema locator,
                                 String locatorRole,
                                 String tableKey) {

        if (locator == null) {
            throw new SchemaValidationException(
                    locatorRole + " missing for table: " + tableKey
            );
        }

        if (isBlank(locator.strategy())) {
            throw new SchemaValidationException(
                    locatorRole + " strategy missing for table: " + tableKey
            );
        }

        if (isBlank(locator.value())) {
            throw new SchemaValidationException(
                    locatorRole + " value missing for table: " + tableKey
            );
        }
    }


    // ---------- Duplicate Key Detection ----------
    private void validateNoDuplicateKeys(PageSchema schema) {
        Set<String> keys = new HashSet<>();

        for (FieldSchema field : schema.fields()) {
            addKey(keys, field.key());
        }

        for (CompositeFieldSchema composite : schema.compositeFields()) {
            addKey(keys, composite.key());
            for (FieldSchema component : composite.components()) {
                addKey(keys, component.key());
            }
        }
    }

    private void addKey(Set<String> keys, String key) {
        if (!keys.add(key)) {
            throw new SchemaValidationException(
                    "Duplicate key detected: " + key
            );
        }
    }

    // ---------- Utils ----------
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
