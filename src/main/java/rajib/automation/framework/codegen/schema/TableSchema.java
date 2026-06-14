package rajib.automation.framework.codegen.schema;

import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;

import java.util.List;

public record TableSchema(

        String key,                    // logical name: usersTable

        LocatorSchema tableLocator,    // root of table/grid

        LocatorSchema rowLocator,      // how to find row units

        LocatorSchema cellLocator,     // how to find cells inside a row

        List<TableColumnSchema> columns

) {}
