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
        sb.append("import rajib.automation.framework.pages.BasePage;\n");
        sb.append("import rajib.automation.framework.model.PageField;\n");
        sb.append("import rajib.automation.framework.enums.FieldType;\n");
        sb.append("import java.util.*;\n");

        // ---- Table-related imports (only if tables exist) ----
        if (schema.tables() != null && !schema.tables().isEmpty()) {
            sb.append("import rajib.automation.framework.model.TableSchema;\n");
            sb.append("import rajib.automation.framework.model.TableColumnSchema;\n");
            sb.append("import rajib.automation.framework.model.LocatorSchema;\n");
            sb.append("import rajib.automation.framework.annotations.PageTables;\n");
        }

        sb.append("\n");

        // ==================================================
        // Class declaration
        // ==================================================
        sb.append("public class ")
                .append(schema.pageName())
                .append(" extends BasePage {\n\n");


        // ==================================================
        // Table metadata (CLASS-LEVEL)
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
        // 1️⃣ Generate atomic PageFields
        // ==================================================
        for (FieldSchema field : schema.fields()) {
            emitPageField(sb, field);
        }

        // ==================================================
        // 2️⃣ Generate composite component PageFields
        // ==================================================
        for (CompositeFieldSchema composite : schema.compositeFields()) {
            for (FieldSchema component : composite.components()) {
                emitPageField(sb, component);
            }
        }

        // ==================================================
        // 3️⃣ Register composite relationships
        // ==================================================
        for (CompositeFieldSchema composite : schema.compositeFields()) {

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

        sb.append("    }\n");
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

}
