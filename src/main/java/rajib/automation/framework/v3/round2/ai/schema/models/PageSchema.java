package rajib.automation.framework.v3.round2.ai.schema.models;



import java.util.List;

public class PageSchema {
    public List<FieldSchema> fields;

    // Required by Jackson
    public PageSchema() {}

    public PageSchema(List<FieldSchema> fields) {
        this.fields = fields;
    }

    public List<FieldSchema> getFields() {
        return fields;
    }

    public void setFields(List<FieldSchema> fields) {
        this.fields = fields;
    }
}