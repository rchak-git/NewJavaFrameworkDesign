package rajib.automation.framework.v3.round2.ai.schema.models;

import rajib.automation.framework.enums.FieldType;
import java.util.Map;

public class FieldSchema extends BaseSchema {
    public FieldType fieldType;
    public Map<String, LocatorSchema> locators;

    public FieldSchema() {}

    public FieldSchema(String key, FieldType fieldType, String logicalName, Map<String, LocatorSchema> locators) {
        super(key, logicalName);
        this.fieldType = fieldType;
        this.locators = locators;
    }

    public FieldType getFieldType() { return fieldType; }
    public void setFieldType(FieldType fieldType) { this.fieldType = fieldType; }

    public Map<String, LocatorSchema> getLocators() { return locators; }
    public void setLocators(Map<String, LocatorSchema> locators) { this.locators = locators; }
}