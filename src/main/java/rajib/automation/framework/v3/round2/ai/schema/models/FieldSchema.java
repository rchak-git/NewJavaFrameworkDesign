package rajib.automation.framework.v3.round2.ai.schema.models;

import rajib.automation.framework.enums.FieldType;
import java.util.Map;

public class FieldSchema {
    public String key;
    public FieldType fieldType; // UPDATED: Now uses enum!
    public String logicalName;
    public Map<String, LocatorSchema> locators;

    // Required by Jackson
    public FieldSchema() {}

    public FieldSchema(String key, FieldType fieldType, String logicalName, Map<String, LocatorSchema> locators) {
        this.key = key;
        this.fieldType = fieldType;
        this.logicalName = logicalName;
        this.locators = locators;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public FieldType getFieldType() { return fieldType; }
    public void setFieldType(FieldType fieldType) { this.fieldType = fieldType; } // param is FieldType

    public String getLogicalName() { return logicalName; }
    public void setLogicalName(String logicalName) { this.logicalName = logicalName; }

    public Map<String, LocatorSchema> getLocators() { return locators; }
    public void setLocators(Map<String, LocatorSchema> locators) { this.locators = locators; }
}