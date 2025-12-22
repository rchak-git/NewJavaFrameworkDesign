package rajib.automation.framework.base;

import agent.FailureAnalysisAgent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import core.context.FailureContext;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.model.TestData;
import rajib.automation.framework.utils.RetryUtils;
import rajib.automation.framework.utils.WaitUtils;
import reporting.ReportManager;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BasePage {

    protected TestData testData;
    // Always fetch the correct thread-local driver
    protected WebDriver driver() {

        return DriverFactory.getDriver();
    }

    protected Map<String, PageField> pageFields = new LinkedHashMap<>();

    /**
     * Centralized element resolution.
     * - Deterministic
     * - No retries
     * - Future AI / healing extension point
     */
    protected WebElement resolveElement(By locator) {
        try {
            WaitUtils.waitForVisible(locator);
            return driver().findElement(locator);

        } catch (Exception e) {

        FailureContext   context = new FailureContext(
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    driver().getCurrentUrl(),
                    driver().getTitle(),
                    "Resolve element: " + locator
            );

            String aiAnalysis =
                    new FailureAnalysisAgent().analyze(context);

            ReportManager.warning(
                    "AI Failure Analysis:\n" + aiAnalysis
            );

            throw e; // IMPORTANT: do NOT swallow exception
        }
    }


    /* -------------------------
       Low-level wrapped actions
       ------------------------- */

    protected String resolveData(PageField field) {

        if (field.getDataKey() == null) {
            return null;
        }

        if (testData == null) {
            throw new IllegalStateException(
                    "TestData not set before executing page actions");
        }

        return testData.get(field.getDataKey());
    }


    public void setTestData(TestData testData) {
        this.testData = testData;
    }
    public void verifyField(String fieldName, TestData testData) {

        PageField field = pageFields.get(fieldName);

        if (field == null) {
            throw new RuntimeException(
                    "No field found with name: " + fieldName
            );
        }

        verifyField(field, testData); // delegate to internal method
    }
    public void verifyField(PageField field, TestData testData) {

        // Skip non-verifiable fields (important!)
        if (field.getValidationType() == null ||
                field.getFieldType() == FieldType.ACTION) {
            return;
        }

        WebElement element = resolveElement(field.getLocator());
        ValidationType validationType = field.getValidationType();

        String expected = field.getExpectedValue();
        if (expected == null && field.getDataKey() != null) {
            expected = testData.get(field.getDataKey());
        }

        switch (validationType) {

            case TEXT_EQUALS:
                Assert.assertEquals(
                        element.getText().trim(),
                        expected,
                        "Text mismatch for field: " + field.getFieldName()
                );
                break;

            case TEXT_CONTAINS:
                Assert.assertTrue(
                        element.getText().contains(expected),
                        "Expected text not found for field: " + field.getFieldName()
                );
                break;

            case IS_VISIBLE:
                Assert.assertTrue(
                        element.isDisplayed(),
                        "Expected field to be visible: " + field.getFieldName()
                );
                break;

            case IS_NOT_VISIBLE:
                Assert.assertFalse(
                        element.isDisplayed(),
                        "Expected field to be hidden: " + field.getFieldName()
                );
                break;

            case IS_ENABLED:
                Assert.assertTrue(
                        element.isEnabled(),
                        "Expected field to be enabled: " + field.getFieldName()
                );
                break;

            case IS_DISABLED:
                Assert.assertFalse(
                        element.isEnabled(),
                        "Expected field to be disabled: " + field.getFieldName()
                );
                break;

            default:
                throw new IllegalStateException(
                        "Unsupported validation type: " + validationType
                );
        }
    }

    public void verifyFields(TestData testData) {
        for (PageField field : pageFields.values()) {
            verifyField(field, testData);
        }
    }

    public void populateFields() {
        for (PageField field : pageFields.values()) {

            if (field.getFieldType() == FieldType.ACTION) continue;
            if (field.getDataKey() == null) continue;

            populateField(field.getFieldName());
        }
    }




    public void populateField(String fieldName) {

        PageField field = pageFields.values().stream()
                .filter(f -> f.getFieldName().equalsIgnoreCase(fieldName))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No field found with name: " + fieldName));

        switch (field.getFieldType()) {

            case TEXTBOX:
                type(field.getLocator(), resolveData(field));
                ReportManager.info(
                        "Framework plumbing check: populateField() executed for field = "
                                + field.getFieldName()
                );
                break;

            case DROPDOWN:
                selectDropdown(field.getLocator(), resolveData(field));
                break;

            case CHECKBOX:
                setCheckbox(field.getLocator(),
                        Boolean.parseBoolean(resolveData(field)));
                break;

            default:
                throw new UnsupportedOperationException(
                        "Unsupported FieldType: " + field.getFieldType());
        }
    }



    protected void click(By locator) {
        RetryUtils.retryOnException(
                () -> resolveElement(locator).click(),
                StaleElementReferenceException.class,
                ElementClickInterceptedException.class
        );
    }

    protected void type(By locator, String text) {
        RetryUtils.retryOnException(
                () -> {
                    WebElement el = resolveElement(locator);
                    el.clear();
                    el.sendKeys(text);
                },
                StaleElementReferenceException.class
        );
    }

    public void performAction(String fieldName) {

        PageField field = getPageFields().stream()
                .filter(f -> f.getFieldName().equalsIgnoreCase(fieldName))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No action field found with name: " + fieldName)
                );

        if (field.getFieldType() != FieldType.ACTION) {
            throw new IllegalArgumentException(
                    fieldName + " is not an ACTION field");
        }

        click(field.getLocator());
    }


    protected String getText(By locator) {

        return resolveElement(locator).getText();
    }

    protected void selectDropdown(By locator, String visibleText) {
        WaitUtils.waitForVisible(locator);
        Select select = new Select(resolveElement(locator));
        select.selectByVisibleText(visibleText);
    }

    protected void setCheckbox(By locator, boolean shouldBeChecked) {
        WaitUtils.waitForClickable(locator);
        boolean isChecked = resolveElement(locator).isSelected();

        if (isChecked != shouldBeChecked) {
            resolveElement(locator).click();
        }
    }

    protected Collection<PageField> getPageFields() {
        return pageFields.values();
    }
}
