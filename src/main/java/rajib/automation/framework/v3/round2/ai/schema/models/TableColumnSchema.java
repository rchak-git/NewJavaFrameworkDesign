package rajib.automation.framework.v3.round2.ai.schema.models;

import rajib.automation.framework.enums.FieldType;

public class TableColumnSchema {

    private String name;
    private int index;
    private FieldType type;

    // Required by Jackson
    public TableColumnSchema() {}

    public TableColumnSchema(String name, int index, FieldType type) {
        this.name = name;
        this.index = index;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }
}