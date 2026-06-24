package rajib.automation.framework.v3.round2.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.TableColumnSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.TableSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TableControl extends BaseControl {

    public TableControl(TableSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }

    private TableSchema tableSchema() {
        return (TableSchema) schema;
    }

    @Override
    public void populate(ControlCommand command) {
        throw new UnsupportedOperationException(
                "Table population not supported yet"
        );
    }

    @Override
    public void verify(ControlCommand command) {
        List<Map<String, String>> rows = readAsRows();

        Object expectedValue = command.getValue();
        if (expectedValue == null) {
            throw new IllegalArgumentException(
                    "VERIFY command for table '" + key() + "' requires expected value(s)."
            );
        }

        Map<String, Object> expectedMap = normalizeExpected(expectedValue);
        ValidationType validationType = extractValidationType(command);

        boolean matched = rows.stream().anyMatch(row -> rowMatches(row, expectedMap, validationType));

        if (!matched) {
            throw new AssertionError(
                    "No matching row found in table '" + key() + "'. Expected: " + expectedMap +
                            ", validationType=" + validationType +
                            ", actualRows=" + rows
            );
        }
    }

    @Override
    public Object read() {
        return readAsRows();
    }

    private List<Map<String, String>> readAsRows() {
        TableSchema tableSchema = tableSchema();

        WebElement tableRoot = resolver.resolve(tableSchema);

        List<WebElement> rows = tableRoot.findElements(toBy(tableSchema.getRowLocator()));
        List<Map<String, String>> result = new ArrayList<>();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(toBy(tableSchema.getCellLocator()));
            Map<String, String> rowData = new LinkedHashMap<>();

            for (TableColumnSchema column : tableSchema.getColumns()) {
                int idx = column.getIndex() - 1;
                String cellText = (idx >= 0 && idx < cells.size())
                        ? cells.get(idx).getText().trim()
                        : null;

                rowData.put(column.getName(), cellText);
            }

            result.add(rowData);
        }

        return result;
    }

    private By toBy(LocatorSchema locator) {
        String strategy = locator.getStrategy();
        String value = locator.getValue();

        return switch (strategy.toLowerCase()) {
            case "id" -> By.id(value);
            case "name" -> By.name(value);
            case "css" -> By.cssSelector(value);
            case "xpath" -> By.xpath(value);
            case "class" -> By.className(value);
            case "tag" -> By.tagName(value);
            default -> throw new IllegalArgumentException(
                    "Unsupported locator strategy: " + strategy
            );
        };
    }

    private ValidationType extractValidationType(ControlCommand command) {
        if (command.getType() == null) {
            return ValidationType.TEXT_EQUALS;
        }
        if (command.getType() instanceof ValidationType vt) {
            return vt;
        }
        throw new IllegalArgumentException(
                "Unsupported validation type for table '" + key() + "': " + command.getType()
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> normalizeExpected(Object expectedValue) {
        if (expectedValue instanceof Map<?, ?> map) {
            Map<String, Object> normalized = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                normalized.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            return normalized;
        }

        throw new IllegalArgumentException(
                "Expected table verification value must be a Map<String, Object> for table '" + key() + "'. " +
                        "Received: " + expectedValue.getClass().getName()
        );
    }

    private boolean rowMatches(
            Map<String, String> actualRow,
            Map<String, Object> expectedRow,
            ValidationType validationType
    ) {
        for (Map.Entry<String, Object> expectedEntry : expectedRow.entrySet()) {
            String columnKey = expectedEntry.getKey();
            String expected = expectedEntry.getValue() == null ? null : String.valueOf(expectedEntry.getValue());
            String actual = actualRow.get(columnKey);

            if (!matches(actual, expected, validationType)) {
                return false;
            }
        }
        return true;
    }

    private boolean matches(String actual, String expected, ValidationType validationType) {
        if (validationType == null) {
            validationType = ValidationType.TEXT_EQUALS;
        }

        if (validationType == ValidationType.DEFAULT) {
            validationType = ValidationType.TEXT_EQUALS;
        }

        if (expected == null) {
            return actual == null;
        }

        if (actual == null) {
            return false;
        }

        return switch (validationType) {
            case TEXT_EQUALS, STATIC_TEXT -> Objects.equals(actual.trim(), expected.trim());
            case TEXT_CONTAINS -> actual.trim().contains(expected.trim());
            default -> Objects.equals(actual.trim(), expected.trim());
        };
    }
}