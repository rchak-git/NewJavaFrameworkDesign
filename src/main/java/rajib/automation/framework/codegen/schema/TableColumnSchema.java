package rajib.automation.framework.codegen.schema;

import rajib.automation.framework.enums.FieldType;

public record TableColumnSchema(

        String name,           // business name (Username, Role, Edit)

        int index,             // cell index in row

        FieldType type          // TEXT, DROPDOWN, CHECKBOX, BUTTON, LINK

) {}
