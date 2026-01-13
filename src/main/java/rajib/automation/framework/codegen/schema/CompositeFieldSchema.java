package rajib.automation.framework.codegen.schema;

import rajib.automation.framework.enums.CompositeType;
import java.util.List;

public record CompositeFieldSchema(
        String key,
        CompositeType compositeType,
        List<FieldSchema> components
) {}
