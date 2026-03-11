package rajib.automation.framework.codegen.schema;

import java.util.List;

public record PageSchema(
        String pageName,
        String framework,
        List<FieldSchema> fields,
        List<CompositeFieldSchema> compositeFields,
        List<TableSchema> tables,
        List<ComponentSchema> components // ✅ NEW (optional)
) {}
