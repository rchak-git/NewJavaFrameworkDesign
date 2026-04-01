package rajib.automation.framework.codegen.schema;

import rajib.automation.framework.enums.FieldType;

import java.util.Map;

public record FieldSchema(
        String key,
        FieldType fieldType,
        String logicalName,
        Map<String, LocatorSchema> locators
) {
    // Backward-compatible static factory for simple controls
    public static FieldSchema ofSimple(
            String key,
            FieldType fieldType,
            String logicalName,
            LocatorSchema locator
    ) {
        return new FieldSchema(
                key,
                fieldType,
                logicalName,
                Map.of("main", locator)
        );
    }

    // Returns the default/main locator (for simple controls)
    public LocatorSchema locator() {
        return locators.get("main");
    }

    // Returns a named locator (for complex controls)
    public LocatorSchema locator(String name) {
        return locators.get(name);
    }
}