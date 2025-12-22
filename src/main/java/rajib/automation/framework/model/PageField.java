package rajib.automation.framework.model;


import org.openqa.selenium.By;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.enums.ValidationType;

public class PageField {

    private final String fieldName;
    private final By locator;
    private final FieldType fieldType;
    private final String dataKey;

    private final ValidationType validationType;
    private final String expectedValue;
    public PageField(String fieldName, By locator, FieldType fieldType, String dataKey) {
        this.fieldName = fieldName;
        this.locator = locator;
        this.fieldType = fieldType;
        this.dataKey = dataKey;
        this.validationType = null;
        this.expectedValue = null;

    }

    public PageField(String fieldName,
                     By locator,
                     FieldType fieldType,
                     String dataKey,
                     ValidationType validationType,
                     String expectedValue) {

        this.fieldName = fieldName;
        this.locator = locator;
        this.fieldType = fieldType;
        this.dataKey = dataKey;
        this.validationType = validationType;
        this.expectedValue = expectedValue;
    }
    public String getFieldName() {
        return fieldName;
    }

    public By getLocator() {
        return locator;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public String getDataKey() {
        return dataKey;
    }

    public ValidationType getValidationType() {
        return validationType;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

}
