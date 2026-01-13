package rajib.automation.framework.codegen.schema;

import rajib.automation.framework.enums.FieldType;

public record FieldSchema(
        String key,
        FieldType fieldType,
        String logicalName,
        LocatorSchema locator
) {}
