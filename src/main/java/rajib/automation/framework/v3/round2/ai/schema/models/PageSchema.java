package rajib.automation.framework.v3.round2.ai.schema.models;

import java.util.List;

public class PageSchema {

    public List<FieldSchema> fields;
    public List<TableSchema> tables;

    // Required by Jackson
    public PageSchema() {}

    public PageSchema(List<FieldSchema> fields, List<TableSchema> tables) {
        this.fields = fields;
        this.tables = tables;
    }

    public List<FieldSchema> getFields() {
        return fields;
    }

    public void setFields(List<FieldSchema> fields) {
        this.fields = fields;
    }

    public List<TableSchema> getTables() {
        return tables;
    }

    public void setTables(List<TableSchema> tables) {
        this.tables = tables;
    }
}