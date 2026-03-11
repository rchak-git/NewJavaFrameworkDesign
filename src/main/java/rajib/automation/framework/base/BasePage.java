package rajib.automation.framework.base;

import agent.FailureAnalysisAgent;
import core.context.FailureContext;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import rajib.automation.framework.codegen.schema.*;
import rajib.automation.framework.codegen.validation.TableSchemaValidator;
import rajib.automation.framework.enums.ExecutionPhase;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.intent.VerifySpec;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.resolution.ComponentResolver;
import rajib.automation.framework.resolution.ResolvedIntent;
import rajib.automation.framework.steps.StepLog;
import rajib.automation.framework.tables.actions.TableActionExecutor;
import rajib.automation.framework.tables.fluent.TableActions;
import rajib.automation.framework.tables.reader.TableReader;
import rajib.automation.framework.utils.LocatorUtils;
import rajib.automation.framework.utils.RetryUtils;
import rajib.automation.framework.utils.WaitUtils;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.resolver.ElementResolver;
import rajib.automation.framework.v2.resolver.PageResolver;
import rajib.automation.framework.v2.runtime.RuntimeValueResolver;
import rajib.automation.framework.v3.verify.VerificationExecutor;
import reporting.ReportManager;
import org.openqa.selenium.NoSuchElementException;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.*;
import rajib.automation.framework.introspection.PageTableExtractor;

import static rajib.automation.framework.utils.LocatorUtils.buildBy;


public abstract class BasePage {

    protected Map<String, PageField> pageFields = new LinkedHashMap<>();
    protected Map<String, List<PageField>> compositePageFields = new LinkedHashMap<>();


    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }

    protected ElementResolver resolver;

    protected BasePage() {
        this.resolver = new PageResolver(); // default page-level resolver
        this.initGlobalComponents();
    }

    protected BasePage(ElementResolver resolver) {
        this.resolver = resolver;
            this.initGlobalComponents();
    }


    protected Map<String, ComponentSchema> componentSchemas = new LinkedHashMap<>();

    public Map<String, ComponentSchema> getComponentSchemas() {
        return componentSchemas;
    }

    protected Map<String, TableSchema> tableSchemaMap() {
        return Map.of(); // default: no tables on this page
    }
    public final TableSchema getTableSchemaOrThrow(String tableKey) {
        TableSchema schema = tableSchemaMap().get(tableKey);
        if (schema == null) {
            throw new IllegalArgumentException("No TableSchema registered for key: " + tableKey);
        }
        return schema;
    }
    protected boolean isComponentKey(String key) {
        return componentSchemas.containsKey(key);
    }

    protected void verifyComponentField(
            String componentKey,
            WebElement root,
            FieldSchema fieldSchema,
            VerifySpec verifySpec
    ) {
        By by = buildBy(
                fieldSchema.locator().strategy(),
                fieldSchema.locator().value()
        );

        WebElement element = root.findElement(by);

        applyVerification(
                fieldSchema.key(),
                fieldSchema.fieldType(),
                element,
                verifySpec
        );
    }


    protected void waitForSpinnerToDisappear() {

        WebDriverWait wait = new WebDriverWait(this.driver(), Duration.ofSeconds(10));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".ngx-spinner-overlay")
        ));
    }

    protected void initGlobalComponents() {

        componentSchemas.put("headerNav",
                new ComponentSchema(
                        "headerNav",
                        new LocatorSchema("css", "nav"),
                        "SINGLE",
                        null,
                        List.of(
                                new FieldSchema(
                                        "home",
                                        FieldType.ACTION,
                                        null,
                                        new LocatorSchema("xpath", ".//button[normalize-space()='HOME']")
                                ),
                                new FieldSchema(
                                        "orders",
                                        FieldType.ACTION,
                                        null,
                                        new LocatorSchema("xpath", ".//button[normalize-space()='ORDERS']")
                                ),
                                new FieldSchema(
                                        "cart",
                                        FieldType.ACTION,
                                        null,
                                        new LocatorSchema("xpath", ".//button[contains(.,'Cart')]")
                                ),
                                new FieldSchema(
                                        "signOut",
                                        FieldType.ACTION,
                                        null,
                                        new LocatorSchema("xpath", ".//button[normalize-space()='Sign Out']")
                                )
                        )
                )
        );
    }

    public ComponentSchema getComponentOrThrow(String key) {
        ComponentSchema schema = componentSchemas.get(key);
        if (schema == null) {
            throw new IllegalArgumentException("No ComponentSchema registered for key: " + key);
        }
        return schema;
    }

    private void handleComponentBlock(String componentKey, Object value) {

        // Step 1: Validate component block must be a Map
        if (!(value instanceof Map<?, ?>)) {
            throw new IllegalArgumentException(
                    "Component block must be a Map of identifier → field map. Component: "
                            + componentKey
            );
        }

        Map<?, ?> componentInstances = (Map<?, ?>) value;

        // Step 2: Validate at least one instance provided
        if (componentInstances.isEmpty()) {
            throw new IllegalArgumentException(
                    "Component block is empty for component: " + componentKey
            );
        }

        ComponentSchema schema = getComponentOrThrow(componentKey);

        // Step 3: Iterate over each identifier entry
        for (Map.Entry<?, ?> instanceEntry : componentInstances.entrySet()) {

            String identifierValue = String.valueOf(instanceEntry.getKey());
            Object fieldsObj = instanceEntry.getValue();

            // Step 4: Validate inner field map
            if (!(fieldsObj instanceof Map<?, ?>)) {
                throw new IllegalArgumentException(
                        "Component instance must contain field map. Component: "
                                + componentKey + ", identifier: " + identifierValue
                );
            }

            Map<?, ?> fieldMap = (Map<?, ?>) fieldsObj;

            if (fieldMap.isEmpty()) {
                throw new IllegalArgumentException(
                        "Field map is empty for component: "
                                + componentKey + ", identifier: " + identifierValue
                );
            }

            WebElement root = resolveComponentRoot(schema, identifierValue);

            // 🔹 Swap resolver to component-scoped resolver
            ElementResolver originalResolver = this.resolver;
            this.resolver = new ComponentResolver(root);

            try {
                for (Map.Entry<?, ?> fieldEntry : fieldMap.entrySet()) {

                    String fieldKey = String.valueOf(fieldEntry.getKey());
                    Object fieldValue = fieldEntry.getValue();

                    // ✅ ONLY populate logic
                    populateField(fieldKey, fieldValue);
                }
            } finally {
                // Always restore original resolver
                this.resolver = originalResolver;
            }
        }
    }

    private void handleComponentVerificationBlock(String componentKey, Object value) {

        if (!(value instanceof Map<?, ?> componentInstances)) {
            throw new IllegalArgumentException(
                    "Component verification block must be Map. Component: " + componentKey
            );
        }

        ComponentSchema schema = getComponentOrThrow(componentKey);

        for (Map.Entry<?, ?> instanceEntry : componentInstances.entrySet()) {

            String identifierValue = String.valueOf(instanceEntry.getKey());
            Object innerObj = instanceEntry.getValue();

            if (!(innerObj instanceof Map<?, ?> innerMap)) {
                throw new IllegalArgumentException(
                        "Component identifier block must be Map. Component: "
                                + componentKey + ", identifier: " + identifierValue
                );
            }

            Object verifyBlockObj = innerMap.get("verify");

            if (verifyBlockObj == null) {
                continue; // nothing to verify for this identifier
            }

            if (!(verifyBlockObj instanceof Map<?, ?> verifyMap)) {
                throw new IllegalArgumentException(
                        "Verify block must be Map. Component: "
                                + componentKey + ", identifier: " + identifierValue
                );
            }

            WebElement root = resolveComponentRoot(schema, identifierValue);

            handleComponentVerification(
                    schema,
                    root,
                    identifierValue,
                    verifyMap
            );
        }
    }

    private void handleComponentVerification(
            ComponentSchema schema,
            WebElement root,
            String identifierValue,
            Map<?, ?> verifyMap
    ) {

        for (Map.Entry<?, ?> verifyEntry : verifyMap.entrySet()) {

            String fieldKey = String.valueOf(verifyEntry.getKey());
            Object verifySpecObj = verifyEntry.getValue();

            if (!(verifySpecObj instanceof Map<?, ?> specMap)) {
                throw new IllegalArgumentException(
                        "Verification spec must be a Map. Component: "
                                + schema.key() + ", identifier: " + identifierValue
                );
            }

            Object typeObj = specMap.get("type");
            if (typeObj == null) {
                throw new IllegalArgumentException(
                        "VerifySpec.type is required. Component: "
                                + schema.key() + ", identifier: " + identifierValue
                                + ", field: " + fieldKey
                );
            }

            String typeStr = String.valueOf(typeObj);
            Object expectedValue = specMap.get("expectedValue");

            ValidationType validationType = ValidationType.valueOf(typeStr);

            VerifySpec verifySpec = new VerifySpec(validationType, expectedValue);

            // ✅ NEW: resolve the component FieldSchema by key
            FieldSchema fieldSchema = schema.fieldOrThrow(fieldKey);

            // ✅ Correct signature
            verifyComponentField(
                    schema.key(),
                    root,
                    fieldSchema,
                    verifySpec
            );
        }
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


    public PageField getFieldOrThrow(String fieldKey) {
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
            return resolver.resolve(locator);
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

    protected void performComponentAction(
            String componentKey,
            WebElement root,
            String fieldKey) {

        ComponentSchema schema = componentSchemas.get(componentKey);

        if (schema == null) {
            throw new RuntimeException("Component not registered: " + componentKey);
        }

        FieldSchema fieldSchema = schema.fields()
                .stream()
                .filter(f -> f.key().equals(fieldKey))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No action field found with key: " + fieldKey)
                );

        ElementResolver originalResolver = this.resolver;
        this.resolver = new ComponentResolver(root);

        try {
            By by = buildBy(
                    fieldSchema.locator().strategy(),
                    fieldSchema.locator().value()
            );

            waitForSpinnerToDisappear();
            WebElement element = resolver.resolve(by);
          //  WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(10));
          //  wait.until(ExpectedConditions.elementToBeClickable(element));
            click(element);

        } finally {
            this.resolver = originalResolver;
        }
    }

    public void performComponentAction(String componentKey, String fieldKey) {

        ComponentSchema schema = componentSchemas.get(componentKey);

        if (schema == null) {
            throw new RuntimeException("Component not registered: " + componentKey);
        }

        if (!schema.isSingle()) {
            throw new IllegalStateException(
                    "Component requires identifier: " + componentKey
            );
        }

        By rootBy = buildBy(
                schema.root().strategy(),
                schema.root().value()
        );

        WebElement root = resolver.resolve(rootBy);

        performComponentAction(componentKey, root, fieldKey);
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
        WebElement element = resolver.resolve(locator);
        ((JavascriptExecutor) driver()).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                element
        );
    }

    protected WebElement resolveComponentRoot(
            String componentKey,
            String identifierValue
    ) {

        ComponentSchema schema = componentSchemas.get(componentKey);

        if (schema == null) {
            throw new RuntimeException("Component not registered: " + componentKey);
        }

        return resolveComponentRoot(schema, identifierValue);
    }

    protected WebElement resolveComponentRoot(
            ComponentSchema schema,
            String identifierValue
    ) {

        // Step 1: Build By for root locator
        By rootBy = buildBy(schema.root());
       WaitUtils.waitForVisible(rootBy,10);
        List<WebElement> roots = driver().findElements(rootBy);

        if (roots.isEmpty()) {
            throw new IllegalArgumentException(
                    "No component roots found for component: " + schema.key()
            );
        }

        // Step 2: Ensure identifierField is defined
        String identifierFieldKey = schema.identifierField();

        if (identifierFieldKey == null) {
            throw new IllegalStateException(
                    "identifierField is required for identifier-based resolution. Component: "
                            + schema.key()
            );
        }

        // Step 3: Get identifier field schema
        FieldSchema identifierField = schema.fieldOrThrow(identifierFieldKey);

        By identifierBy = buildBy(identifierField.locator());

        // Step 4: Iterate roots and match identifier text
        for (WebElement root : roots) {

            try {
                WebElement identifierElement = root.findElement(identifierBy);
                String actualText = identifierElement.getText();
                System.out.println("Comparing: [" + actualText + "] with [" + identifierValue + "]");
                if (actualText != null &&
                        actualText.trim().equals(identifierValue.trim())) {

                    return root;
                }

            } catch (NoSuchElementException ignored) {
                // This root does not contain identifier field — skip
            }
        }

        // Step 5: If no match found
        throw new IllegalArgumentException(
                "Component instance not found. component=" + schema.key()
                        + ", identifier=" + identifierValue
        );
    }


    public void populate(Map<String, Object> testData) {

        if (testData == null || testData.isEmpty()) {
            return;
        }

        System.out.println("Incoming keys: " + testData.keySet());
        for (Map.Entry<String, Object> entry : testData.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();

            if (isComponentKey(key)) {
                handleComponentBlock(key, value);
            } else {
                populateField(key, value);
            }
        }
    }


    @SuppressWarnings("unchecked")
    public void populate(Map<String, Object> testData, RuntimeContext context) {

        if (testData == null || testData.isEmpty()) {
            return;
        }

        RuntimeValueResolver resolver = new RuntimeValueResolver();

        for (Map.Entry<String, Object> entry : testData.entrySet()) {

            String key = entry.getKey();
            Object rawValue = entry.getValue();
            Object resolvedValue = rawValue;

            // -------------------------------------------------
            // Resolve runtime placeholders (only for String)
            // -------------------------------------------------
            if (rawValue instanceof String str) {
                resolvedValue = resolver.resolve(str, context);
            }

            // -------------------------------------------------
            // 🔥 Composite Handling
            // -------------------------------------------------
            if (compositePageFields.containsKey(key)) {

                List<PageField> compositeFields = compositePageFields.get(key);

                if (compositeFields == null || compositeFields.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Composite key '" + key + "' has no registered fields"
                    );
                }

                // ---------------------------------------------
                // Case 1: RADIO group (value = selected key)
                // ---------------------------------------------
                if (resolvedValue instanceof String selectedKey) {

                    PageField selectedField = pageFields.get(selectedKey);

                    if (selectedField == null) {
                        throw new IllegalArgumentException(
                                "Invalid selection '" + selectedKey +
                                        "' for composite '" + key + "'"
                        );
                    }

                    populateField(selectedKey, true);
                    continue;
                }

                // ---------------------------------------------
                // Case 2: Composite dropdown (value = Map)
                // ---------------------------------------------
                if (resolvedValue instanceof Map<?, ?> map) {

                    Map<String, Object> subValues = (Map<String, Object>) map;

                    for (Map.Entry<String, Object> subEntry : subValues.entrySet()) {

                        String subKey = subEntry.getKey();
                        Object subRawValue = subEntry.getValue();
                        Object subResolvedValue = subRawValue;

                        if (subRawValue instanceof String subStr) {
                            subResolvedValue = resolver.resolve(subStr, context);
                        }

                        populateField(subKey, subResolvedValue);
                    }

                    continue;
                }

                throw new IllegalArgumentException(
                        "Unsupported value type for composite '" + key +
                                "': " + resolvedValue.getClass().getSimpleName()
                );
            }

            // -------------------------------------------------
            // Normal atomic field handling
            // -------------------------------------------------
            populateField(key, resolvedValue);
        }
    }
    public void populateField(String fieldKey, Object value) {


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

                By target = field.getLocator();

                try {
                    scrollIntoView(target);
                    click(target);
                } catch (ElementClickInterceptedException e) {
                    jsClick(target);
                }
            }
            break;
            case RADIO: {
                boolean select = Boolean.parseBoolean(resolvedValue);
                if (!select) return;

                By target = field.getLocator();

                try {
                    scrollIntoView(target);
                    click(target);
                } catch (ElementClickInterceptedException e) {
                    jsClick(target);
                }
                break;
            }
            case STATIC_TEXT:
                // STATIC_TEXT is read-only. Do nothing during populate.
                return;
            default:
                throw new UnsupportedOperationException(
                        "Unsupported FieldType: " + field.getFieldType()
                );
        }
    }


    public void verify(Map<String, VerifySpec> verifySpecs) {

        if (verifySpecs == null || verifySpecs.isEmpty()) {
            return;
        }

        for (Map.Entry<String, VerifySpec> entry : verifySpecs.entrySet()) {

            String key = entry.getKey();
            VerifySpec verifySpec = entry.getValue();

            // -------------------------------------------------
            // 1️⃣ Component Verification
            // -------------------------------------------------
            if (isComponentKey(key)) {

                Object raw = verifySpec.expectedValue();

                if (!(raw instanceof Map<?, ?> componentBlock)) {
                    throw new IllegalArgumentException(
                            "Component verification block must be Map. Component: " + key
                    );
                }

                handleComponentVerificationBlock(
                        key,
                        (Map<String, Object>) componentBlock
                );

                continue;
            }


            // -------------------------------------------------
            // 2️⃣ Normal Page Field Verification
            // -------------------------------------------------

            PageField field = getFieldOrThrow(key);

            verifyField(key, verifySpec);
        }
    }

    public void verifyField(String fieldKey, VerifySpec verifySpec) {

        PageField field = getFieldOrThrow(fieldKey);

        By locator = field.getLocator();

        WebElement el = WaitUtils.waitForVisible(locator);

        applyVerification(
                fieldKey,
                field.getFieldType(),
                el,
                verifySpec
        );
    }


    private void applyVerification(
            String fieldKey,
            FieldType fieldType,
            WebElement el,
            VerifySpec verifySpec
    ) {
        VerificationExecutor.verifyElement(
                fieldKey,
                fieldType,
                el,
                verifySpec
        );
    }

    private String extractActualValue(FieldType fieldType, WebElement el) {

        switch (fieldType) {

            case TEXTBOX:
                return el.getAttribute("value");

            case STATIC_TEXT:
                return el.getText().trim();

            default:
                return el.getText();
        }
    }


    // ==================================================
    // NEW: Execution Orchestrator
    // ==================================================

    // This method will get deprecated in future
    public void execute(
            Map<String, ResolvedIntent> resolvedIntents,
            ExecutionPhase phase
    ) {

        Set<String> compositeComponentKeys = getCompositeComponentKeys();

        // 1️⃣ First: populate composite controls
        if (phase == ExecutionPhase.POPULATE) {
            for (String compositeKey : compositePageFields.keySet()) {
                populateComposite(compositeKey, resolvedIntents);
            }
        }

        // 2️⃣ Then: populate atomic fields
        for (Map.Entry<String, ResolvedIntent> entry : resolvedIntents.entrySet()) {

            String fieldKey = entry.getKey();

            // 🚫 Skip composite components
            if (compositeComponentKeys.contains(fieldKey)) {
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
                        verifyField(
                                fieldKey,
                                ri.intent().verify().orElseThrow()
                        );
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

    /*
    protected boolean isElementVisible(By locator) {
        try {
            return driver().findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

     */
    protected boolean isElementVisible(By locator) {

        List<WebElement> elements = driver().findElements(locator);

        if (elements.isEmpty()) {
            throw new AssertionError("Element not found for locator: " + locator);
        }

        return elements.get(0).isDisplayed();
    }

    protected void click(By locator) {
        RetryUtils.retryOnException(
                () -> resolveElement(locator).click(),
                StaleElementReferenceException.class,
                ElementClickInterceptedException.class
        );
    }

    protected void click(WebElement element) {

        RetryUtils.retryOnException(
                element::click,
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

        WebElement element = resolver.resolve(locator);
        String tagName = element.getTagName();

        // -------------------------------------------------
        // Standard HTML <select>
        // -------------------------------------------------
        if ("select".equalsIgnoreCase(tagName)) {

            // 🔥 Wait until dropdown becomes enabled
            WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(5));
            wait.until(d -> element.isEnabled());

            Select select = new Select(element);

            // 🔥 Optional: wait until options are loaded
            wait.until(d -> select.getOptions().size() > 1);

            select.selectByVisibleText(value);
            return;
        }

        // -------------------------------------------------
        // Custom dropdown (non-select)
        // -------------------------------------------------
        jsClick(locator);

        WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(5));
        wait.until(d -> element.isEnabled());

        element.sendKeys(value);
        element.sendKeys(Keys.ENTER);
    }

    protected boolean isElementPresent(By locator) {
        try {
            resolver.resolve(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    protected void jsClick(By locator) {
        WebElement element = resolver.resolve(locator);
        ((JavascriptExecutor) driver())
                .executeScript("arguments[0].click();", element);
    }


    public void performComponentActionByIdentifier(
            String componentKey,
            String identifierValue,
            String fieldKey
    ) {

        ComponentSchema schema = componentSchemas.get(componentKey);

        if (schema == null) {
            throw new RuntimeException(
                    "Component not registered: " + componentKey
            );
        }

        // 🔹 Resolve correct root element for identifier
        WebElement root = resolveComponentRoot(schema, identifierValue);

        // 🔹 Delegate to internal protected method
        performComponentAction(componentKey, root, fieldKey);
    }

}
