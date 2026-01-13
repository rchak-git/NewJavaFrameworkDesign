package rajib.automation.framework.model;

import org.openqa.selenium.By;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.enums.ValidationType;

public class PageField {

    private final String fieldName;
    private final By locator;
    private final FieldType fieldType;
    private final String dataKey;

    // Locator metadata (used for RADIO / CHECKBOX label clicks)
    private final String locatorStrategy;   // id | css | xpath
    private final String locatorValue;      // raw locator value

    // Optional page-level validation (legacy / optional)
    private final ValidationType validationType;
    private final String expectedValue;

    // --------------------------------------------------
    // Constructor 1️⃣: Legacy usage (no locator metadata)
    // --------------------------------------------------
    public PageField(String fieldName,
                     By locator,
                     FieldType fieldType,
                     String dataKey) {

        this.fieldName = fieldName;
        this.locator = locator;
        this.fieldType = fieldType;
        this.dataKey = dataKey;

        this.locatorStrategy = null;
        this.locatorValue = null;

        this.validationType = null;
        this.expectedValue = null;
    }

    // --------------------------------------------------
    // Constructor 2️⃣: Legacy + validation (optional)
    // --------------------------------------------------
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

        this.locatorStrategy = null;
        this.locatorValue = null;

        this.validationType = validationType;
        this.expectedValue = expectedValue;
    }

    // --------------------------------------------------
    // Constructor 3️⃣: Generator / Schema-driven usage
    // --------------------------------------------------
    public PageField(String fieldName,
                     String locatorStrategy,
                     String locatorValue,
                     FieldType fieldType,
                     String dataKey) {

        this.fieldName = fieldName;
        this.locatorStrategy = locatorStrategy;
        this.locatorValue = locatorValue;
        this.locator = buildBy(locatorStrategy, locatorValue);
        this.fieldType = fieldType;
        this.dataKey = dataKey;

        this.validationType = null;
        this.expectedValue = null;
    }

    // --------------------------------------------------
    // Locator factory
    // --------------------------------------------------
    private By buildBy(String strategy, String value) {
        switch (strategy.toLowerCase()) {
            case "id":
                return By.id(value);
            case "css":
                return By.cssSelector(value);
            case "xpath":
                return By.xpath(value);
            default:
                throw new IllegalArgumentException(
                        "Unsupported locator strategy: " + strategy
                );
        }
    }

    // --------------------------------------------------
    // Getters
    // --------------------------------------------------
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

    public String getLocatorStrategy() {
        return locatorStrategy;
    }

    public String getLocatorValue() {
        return locatorValue;
    }

    public ValidationType getValidationType() {
        return validationType;
    }

    public String getExpectedValue() {
        return expectedValue;
    }
}
