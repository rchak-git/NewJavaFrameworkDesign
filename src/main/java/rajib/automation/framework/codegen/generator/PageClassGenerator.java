package rajib.automation.framework.codegen.generator;


import rajib.automation.framework.codegen.schema.*;
import rajib.automation.framework.enums.FieldType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PageClassGenerator {

    public String generate(PageSchema schema) {

        StringBuilder sb = new StringBuilder();

        // ==================================================
        // Imports
        // ==================================================
        sb.append("import rajib.automation.framework.base.BasePage;\n");
        sb.append("import rajib.automation.framework.model.PageField;\n");
        sb.append("import rajib.automation.framework.enums.FieldType;\n");
        sb.append("import java.util.*;\n");

        if (schema.tables() != null && !schema.tables().isEmpty()) {
            sb.append("import rajib.automation.framework.model.TableSchema;\n");
            sb.append("import rajib.automation.framework.model.TableColumnSchema;\n");
            sb.append("import rajib.automation.framework.model.LocatorSchema;\n");
            sb.append("import rajib.automation.framework.annotations.PageTables;\n");
        }

        if (schema.components() != null && !schema.components().isEmpty()) {
            sb.append("import rajib.automation.framework.codegen.schema.ComponentSchema;\n");
            sb.append("import rajib.automation.framework.codegen.schema.FieldSchema;\n");
            sb.append("import rajib.automation.framework.model.LocatorSchema;\n");
        }

        sb.append("\n");

        // ==================================================
        // Class declaration
        // ==================================================
        sb.append("public class ")
                .append(schema.pageName())
                .append(" extends BasePage {\n\n");

        // ==================================================
        // Table metadata (CLASS LEVEL)
        // ==================================================
        emitTables(sb, schema);

        // ==================================================
        // Constructor
        // ==================================================
        sb.append("    public ")
                .append(schema.pageName())
                .append("() {\n\n");

        sb.append("        initSchemas();\n\n");

        // ==================================================
        // 1️⃣ Atomic fields
        // ==================================================
        for (FieldSchema field : schema.fields()) {
            emitPageField(sb, field);
        }

        // ==================================================
        // 2️⃣ Composite fields
        // ==================================================
        List<CompositeFieldSchema> composites =
                schema.compositeFields() == null ? List.of() : schema.compositeFields();

        for (CompositeFieldSchema composite : composites) {
            for (FieldSchema component : composite.components()) {
                emitPageField(sb, component);
            }
        }

        // ==================================================
        // 3️⃣ Composite relationships
        // ==================================================
        for (CompositeFieldSchema composite : composites) {

            sb.append("        compositePageFields.put(\"")
                    .append(composite.key())
                    .append("\", new ArrayList<>());\n");

            for (FieldSchema component : composite.components()) {
                sb.append("        compositePageFields.get(\"")
                        .append(composite.key())
                        .append("\")")
                        .append(".add(pageFields.get(\"")
                        .append(component.key())
                        .append("\"));\n");
            }

            sb.append("\n");
        }

        // ==================================================
        // 4️⃣ Components  ✅ MOVED INSIDE CONSTRUCTOR
        // ==================================================
        if (schema.components() != null && !schema.components().isEmpty()) {
            for (ComponentSchema component : schema.components()) {
                emitComponentSchema(sb, component);
            }
        }

        // ==================================================
        // Close constructor
        // ==================================================
        sb.append("    }\n");

        // ==================================================
        // Close class
        // ==================================================
        sb.append("}\n");

        return sb.toString();
    }

    // ==================================================
    // Helper: Emit a PageField safely
    // ==================================================
    private void emitPageField(StringBuilder sb, FieldSchema field) {

        sb.append("        pageFields.put(\"")
                .append(field.key())
                .append("\",\n");

        sb.append("            new PageField(\n")
                .append("                \"")
                .append(field.key())
                .append("\",\n")
                .append("                \"")
                .append(field.locator().strategy())
                .append("\",\n")
                .append("                \"")
                .append(field.locator().value())
                .append("\",\n")
                .append("                FieldType.")
                .append(field.fieldType())
                .append(",\n")
                .append("                \"")
                .append(field.key())
                .append("\"\n")
                .append("            )\n")
                .append("        );\n\n");
    }

    private void emitTables(StringBuilder sb, PageSchema schema) {

        if (schema.tables() == null || schema.tables().isEmpty()) {
            return;
        }

        sb.append("    @PageTables\n");
        sb.append("    private final List<TableSchema> tables = List.of(\n");

        for (int i = 0; i < schema.tables().size(); i++) {
            TableSchema table = schema.tables().get(i);

            sb.append("        new TableSchema(\n");
            sb.append("            \"").append(table.key()).append("\",\n");
            sb.append("            new LocatorSchema(\"")
                    .append(table.tableLocator().strategy()).append("\", \"")
                    .append(table.tableLocator().value()).append("\"),\n");
            sb.append("            new LocatorSchema(\"")
                    .append(table.rowLocator().strategy()).append("\", \"")
                    .append(table.rowLocator().value()).append("\"),\n");
            sb.append("            new LocatorSchema(\"")
                    .append(table.cellLocator().strategy()).append("\", \"")
                    .append(table.cellLocator().value()).append("\"),\n");

            sb.append("            List.of(\n");

            for (int j = 0; j < table.columns().size(); j++) {
                TableColumnSchema column = table.columns().get(j);

                sb.append("                new TableColumnSchema(\"")
                        .append(column.name()).append("\", ")
                        .append(column.index()).append(", FieldType.")
                        .append(column.type()).append(")");

                if (j < table.columns().size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }

            sb.append("            )\n");
            sb.append("        )");

            if (i < schema.tables().size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("    );\n\n");
    }


    private void emitComponentSchema(StringBuilder sb, ComponentSchema component) {

        sb.append("        componentSchemas.put(\"")
                .append(component.key())
                .append("\",\n");

        sb.append("                new ComponentSchema(\n");

        // key
        sb.append("                        \"")
                .append(component.key())
                .append("\",\n");

        // root locator
        sb.append("                        new LocatorSchema(\"")
                .append(component.root().strategy())
                .append("\", \"")
                .append(component.root().value())
                .append("\"),\n");

        // identifierField (nullable)
        if (component.identifierField() != null) {
            sb.append("                        \"")
                    .append(component.identifierField())
                    .append("\",\n");
        } else {
            sb.append("                        null,\n");
        }

        // fields list
        sb.append("                        List.of(\n");

        List<FieldSchema> fields = component.fields();
        for (int i = 0; i < fields.size(); i++) {

            FieldSchema field = fields.get(i);

            sb.append("                                new FieldSchema(\n");
            sb.append("                                        \"")
                    .append(field.key())
                    .append("\",\n");
            sb.append("                                        FieldType.")
                    .append(field.fieldType())
                    .append(",\n");
            sb.append("                                        null,\n"); // logicalName NOT emitted inside components
            sb.append("                                        new LocatorSchema(\"")
                    .append(field.locator().strategy())
                    .append("\", \"")
                    .append(field.locator().value())
                    .append("\")\n");
            sb.append("                                )");

            if (i < fields.size() - 1) {
                sb.append(",");
            }

            sb.append("\n");
        }

        sb.append("                        )\n");
        sb.append("                )\n");
        sb.append("        );\n\n");
    }

}
