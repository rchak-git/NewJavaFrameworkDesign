package rajib.automation.framework.base;

import agent.FailureAnalysisAgent;
import core.context.FailureContext;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import rajib.automation.framework.codegen.schema.CompositeFieldSchema;
import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.codegen.schema.PageSchema;
import rajib.automation.framework.codegen.validation.TableSchemaValidator;
import rajib.automation.framework.enums.ExecutionPhase;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.intent.VerifySpec;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.resolution.ResolvedIntent;
import rajib.automation.framework.steps.StepLog;
import rajib.automation.framework.tables.actions.TableActionExecutor;
import rajib.automation.framework.tables.fluent.TableActions;
import rajib.automation.framework.tables.reader.TableReader;
import rajib.automation.framework.utils.RetryUtils;
import rajib.automation.framework.utils.WaitUtils;
import reporting.ReportManager;
import org.openqa.selenium.NoSuchElementException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.*;
import rajib.automation.framework.introspection.PageTableExtractor;
import rajib.automation.framework.codegen.schema.TableSchema;

public abstract class BasePage {

    protected Map<String, PageField> pageFields = new LinkedHashMap<>();
    protected Map<String, List<PageField>> compositePageFields = new LinkedHashMap<>();


    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }

    protected BasePage() {

       }

    protected final void initSchemas() {

        List<TableSchema> discoveredTables =
                PageTableExtractor.extract(this);

        System.out.println(
                "DEBUG discoveredTables size = " +
                        (discoveredTables == null ? "null" : discoveredTables.size())
        );

        TableSchemaValidator.validate(
                discoveredTables,
                this.getClass().getSimpleName()
        );

        registerTables(discoveredTables);
    }
    // ==================================================
    // Helpers
    // ==================================================

    public TableActions tableExecutor(String tableKey) {

        TableSchema schema = getTable(tableKey);

        TableActionExecutor executor =
                new TableActionExecutor(driver(), schema);

        return new TableActions(executor);
    }

    public TableSchema getTable(String tableKey) {
        TableSchema table = table(tableKey)
                .orElseThrow(() ->
                        new IllegalArgumentException("Table not found: " + tableKey));
        return table;
    }

    public TableReader tableReader(String tableKey) {

        TableSchema schema = table(tableKey)
                .orElseThrow(() ->
                        new IllegalArgumentException("Table not found: " + tableKey));

        return new TableReader(driver(), schema);
    }


    public TableActionExecutor tableActions(String tableKey) {

        TableSchema schema = table(tableKey)
                .orElseThrow(() ->
                        new IllegalArgumentException("Table not found: " + tableKey));

        return new TableActionExecutor(driver(), schema);
    }

    private final Map<String, TableSchema> tableRegistry = new LinkedHashMap<>();

    /**
     * Registers table schemas discovered for this page.
     * WT-1.3 only: store metadata; no execution.
     */
    protected void registerTables(List<TableSchema> tables) {
        if (tables == null) return;

        for (TableSchema t : tables) {

            if (t == null) continue;

            String key = t.key(); // <-- if your getter name differs, adjust this line only
            if (key == null || key.isBlank()) {
                throw new IllegalArgumentException("TableSchema key cannot be null/blank on page: "
                        + this.getClass().getSimpleName());
            }

            // If you prefer "fail on duplicates" instead of overwrite:
            if (tableRegistry.containsKey(key)) {
                throw new IllegalArgumentException("Duplicate table key '" + key + "' on page: "
                        + this.getClass().getSimpleName());
            }

            tableRegistry.put(key, t);
        }
    }

    /** Returns all registered tables for this page (read-only). */
    public Map<String, TableSchema> tables() {
        return Collections.unmodifiableMap(tableRegistry);
    }

    /** Returns a table schema by key, if present. */
    public Optional<TableSchema> table(String key) {
        return Optional.ofNullable(tableRegistry.get(key));
    }


    protected PageField getFieldOrThrow(String fieldKey) {
        PageField field = pageFields.get(fieldKey);
        if (field == null) {
            throw new IllegalArgumentException(
                    "No PageField registered for key: " + fieldKey
            );
        }
        return field;
    }

    protected WebElement resolveElement(By locator) {
        try {
            WaitUtils.waitForVisible(locator);
            return driver().findElement(locator);
        } catch (Exception e) {

            FailureContext context = new FailureContext(
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

            throw e;
        }
    }




    // ==================================================
    // NEW: Value-based primitives (no TestData required)
    // ==================================================

    public void performAction(String fieldKey) {

        PageField field = pageFields.get(fieldKey);

        if (field == null) {
            throw new RuntimeException("No action field found with key: " + fieldKey);
        }

        if (field.getFieldType() != FieldType.ACTION) {
            throw new IllegalArgumentException(fieldKey + " is not an ACTION field");
        }

        try {
            scrollIntoView(field.getLocator());
            click(field.getLocator());
            StepLog.info("Clicked action: " + field.getFieldName());
        } catch (ElementClickInterceptedException e) {
            jsClick(field.getLocator());
            StepLog.info("Clicked action via JS fallback: " + field.getFieldName());
        }
    }


    protected void scrollIntoView(By locator) {
        WebElement element = driver().findElement(locator);
        ((JavascriptExecutor) driver()).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                element
        );
    }


    protected void populateField(String fieldKey, Object value) {


        StepLog.info(
                "POPULATE intent → field=" + fieldKey
                        + ", value=" + value
        );
        PageField field = getFieldOrThrow(fieldKey);

        if (field.getFieldType() == FieldType.ACTION) {
            return;
        }

        String resolvedValue = value == null ? null : String.valueOf(value);

        switch (field.getFieldType()) {

            case TEXTBOX:
                type(field.getLocator(), resolvedValue);
                StepLog.info("Entered " + field.getFieldName());
                break;

            case DROPDOWN:
                selectDropdown(field.getLocator(), resolvedValue);
                StepLog.info("Selected " + field.getFieldName());
                break;

            case CHECKBOX: {
                boolean shouldCheck = Boolean.parseBoolean(resolvedValue);
                if (!shouldCheck) return;

                String inputId = field.getLocatorValue();
                By labelLocator = By.cssSelector("label[for='" + inputId + "']");

                if (isElementPresent(labelLocator)) {
                    click(labelLocator);
                } else {
                    jsClick(field.getLocator());
                }
                break;
            }

            case RADIO: {
                boolean select = Boolean.parseBoolean(resolvedValue);
                if (!select) return;

                String inputId = field.getLocatorValue();
                By labelLocator = By.cssSelector("label[for='" + inputId + "']");

                if (isElementVisible(labelLocator)) {
                    click(labelLocator);
                } else {
                    jsClick(field.getLocator());
                }
                break;
            }

            default:
                throw new UnsupportedOperationException(
                        "Unsupported FieldType: " + field.getFieldType()
                );
        }
    }

    protected void verifyField(String fieldKey, VerifySpec verifySpec) {

        PageField field = getFieldOrThrow(fieldKey);

        // Skip actions
        if (field.getFieldType() == FieldType.ACTION) {
            return;
        }

        ValidationType type =
                verifySpec != null ? verifySpec.type() : null;

        Object expectedObj =
                verifySpec != null ? verifySpec.expectedValue() : null;

        if (type == null) {
            type = ValidationType.TEXT_EQUALS;
        }

        By locator = field.getLocator();
        String expected =
                expectedObj == null ? null : String.valueOf(expectedObj);

        switch (type) {

            case TEXT_EQUALS: {

                // 🔑 React-safe wait
                WaitUtils.waitUntil(() -> {
                    WebElement el = driver().findElement(locator);
                    String actual = el.getAttribute("value");
                    return expected != null && expected.equals(actual);
                }, "Waiting for value to match for field: " + field.getFieldName());

                String actual =
                        driver().findElement(locator).getAttribute("value");

                StepLog.info(
                        "VERIFY TEXT_EQUALS [" + field.getFieldName() + "]"
                                + " | expected=" + expected
                                + " | actual=" + actual
                );

                Assert.assertEquals(
                        actual,
                        expected,
                        "Text mismatch for field: " + field.getFieldName()
                );
                break;
            }

            case TEXT_CONTAINS: {

                WaitUtils.waitUntil(() -> {
                    WebElement el = driver().findElement(locator);
                    String actual = el.getAttribute("value");
                    return actual != null && actual.contains(expected);
                }, "Waiting for value to contain expected text for field: "
                        + field.getFieldName());

                String actual =
                        driver().findElement(locator).getAttribute("value");

                StepLog.info(
                        "VERIFY TEXT_CONTAINS [" + field.getFieldName() + "]"
                                + " | expected fragment=" + expected
                                + " | actual=" + actual
                );

                Assert.assertTrue(
                        actual.contains(expected),
                        "Expected text not found for field: "
                                + field.getFieldName()
                );
                break;
            }

            case IS_VISIBLE: {
                WebElement el = driver().findElement(locator);

                StepLog.info(
                        "VERIFY IS_VISIBLE [" + field.getFieldName() + "]"
                                + " | displayed=" + el.isDisplayed()
                );

                Assert.assertTrue(el.isDisplayed());
                break;
            }

            case IS_NOT_VISIBLE: {
                WebElement el = driver().findElement(locator);

                StepLog.info(
                        "VERIFY IS_NOT_VISIBLE [" + field.getFieldName() + "]"
                                + " | displayed=" + el.isDisplayed()
                );

                Assert.assertFalse(el.isDisplayed());
                break;
            }

            case IS_ENABLED: {
                WebElement el = driver().findElement(locator);

                StepLog.info(
                        "VERIFY IS_ENABLED [" + field.getFieldName() + "]"
                                + " | enabled=" + el.isEnabled()
                );

                Assert.assertTrue(el.isEnabled());
                break;
            }

            case IS_DISABLED: {
                WebElement el = driver().findElement(locator);

                StepLog.info(
                        "VERIFY IS_DISABLED [" + field.getFieldName() + "]"
                                + " | enabled=" + el.isEnabled()
                );

                Assert.assertFalse(el.isEnabled());
                break;
            }

            default:
                throw new IllegalStateException(
                        "Unsupported validation type: " + type
                );
        }
    }


    // ==================================================
    // NEW: Execution Orchestrator
    // ==================================================
    public void execute(Map<String, ResolvedIntent> resolvedIntents,
                        ExecutionPhase phase) {

        Set<String> compositeKeys = getCompositeComponentKeys();

        for (Map.Entry<String, ResolvedIntent> entry : resolvedIntents.entrySet()) {

            String fieldKey = entry.getKey();

            // 🚫 Skip composite components — handled separately
            if (compositeKeys.contains(fieldKey)) {
                continue;
            }

            ResolvedIntent ri = entry.getValue();

            switch (phase) {

                case POPULATE:
                    if (ri.shouldPopulate()) {
                        Object value = ri.intent().populate().orElseThrow();
                        populateField(fieldKey, value);
                    }
                    break;

                case VERIFY:
                    if (ri.shouldVerify()) {
                        verifyField(fieldKey, ri.intent().verify().orElseThrow());
                    }
                    break;
            }
        }
    }

    public void populateComposite(
            String compositeKey,
            Map<String, ResolvedIntent> resolvedIntents
    ) {

        List<PageField> components = compositePageFields.get(compositeKey);

        if (components == null || components.isEmpty()) {
            throw new RuntimeException(
                    "No composite registered with key: " + compositeKey
            );
        }

        StepLog.info("Populating composite control: " + compositeKey);

        for (PageField field : components) {

            ResolvedIntent ri = resolvedIntents.get(field.getFieldName());

            if (ri == null || !ri.shouldPopulate()) {
                continue;
            }

            Object value = ri.intent()
                    .populate()
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Populate intent missing for composite field: "
                                            + field.getFieldName()
                            )
                    );

            populateField(field.getFieldName(), value);

            waitForDependentUpdate();
        }
    }


    protected Set<String> getCompositeComponentKeys() {
        return compositePageFields.values()
                .stream()
                .flatMap(List::stream)
                .map(PageField::getFieldName)
                .collect(Collectors.toSet());
    }



    protected void waitForDependentUpdate() {
        try {
            Thread.sleep(400); // temporary, deterministic
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ==================================================
    // Existing utilities (unchanged)
    // ==================================================

    protected boolean isElementVisible(By locator) {
        try {
            return driver().findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
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

    protected void selectDropdown(By locator, String value) {

        WebElement element = driver().findElement(locator);
        String tagName = element.getTagName();

        if ("select".equalsIgnoreCase(tagName)) {
            new Select(element).selectByVisibleText(value);
            return;
        }

        jsClick(locator);
        element.sendKeys(value);
        element.sendKeys(Keys.ENTER);
    }

    protected boolean isElementPresent(By locator) {
        return !driver().findElements(locator).isEmpty();
    }

    protected void jsClick(By locator) {
        WebElement element = driver().findElement(locator);
        ((JavascriptExecutor) driver())
                .executeScript("arguments[0].click();", element);
    }
}
