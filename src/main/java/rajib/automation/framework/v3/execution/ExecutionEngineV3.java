package rajib.automation.framework.v3.execution;

import org.openqa.selenium.WebDriver;
import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.intent.VerifySpec;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v3.normalization.DefaultTestDataNormalizerV3;


import java.util.*;

public class ExecutionEngineV3 {

    private final DefaultTestDataNormalizerV3 normalizer;
    private final ExecutionDispatcherV3 dispatcher;
    private final WebDriver driver;

    private static final Set<String> RESERVED_NAMESPACES = Set.of(
            "tables",
            "components",
            "pageMeta",
            "escape"
    );

    // ---------------------------------------
    // Constructors
    // ---------------------------------------

    public ExecutionEngineV3() {
        this.normalizer = null;
        this.dispatcher = null;
        this.driver = null;
    }


    public ExecutionEngineV3(DefaultTestDataNormalizerV3 normalizer,
                             ExecutionDispatcherV3 dispatcher,
                             WebDriver driver) {

        this.normalizer = Objects.requireNonNull(normalizer, "normalizer");
        this.dispatcher = Objects.requireNonNull(dispatcher, "dispatcher");
        this.driver = Objects.requireNonNull(driver, "driver");
    }

    // ---------------------------------------
    // Execute (High-level entry)
    // ---------------------------------------

    public void execute(BasePage page,
                        Map<String, Object> rawTestData,
                        RuntimeContext context) {

        if (rawTestData == null || rawTestData.isEmpty()) {
            throw new IllegalArgumentException("TestData is empty");
        }

        executePopulate(page, rawTestData, context);
        executeVerify(page, rawTestData, context);
    }

    // ---------------------------------------
    // Populate
    // ---------------------------------------

    public void executePopulate(BasePage page,
                                Map<String, Object> rawTestData,
                                RuntimeContext context) {

        if (rawTestData == null || rawTestData.isEmpty()) {
            throw new IllegalArgumentException("TestData is empty");
        }

        Object populateBlock = rawTestData.get("populate");

        if (!(populateBlock instanceof Map<?, ?> populateMap)) {
            return; // nothing to populate
        }

        for (Map.Entry<?, ?> entry : populateMap.entrySet()) {

            String key = String.valueOf(entry.getKey());
            Object value = entry.getValue();

            page.populateField(key, value);
        }
    }
    // ---------------------------------------
    // Verify
    // ---------------------------------------

    public void executeVerify(BasePage page,
                              Map<String, Object> rawTestData,
                              RuntimeContext context) {

        if (rawTestData == null || rawTestData.isEmpty()) {
            throw new IllegalArgumentException("TestData is empty");
        }

        Object verifyBlock = rawTestData.get("verify");

        if (!(verifyBlock instanceof Map<?, ?> verifyMap)) {
            return; // nothing to verify
        }

        Map<String, VerifySpec> verifySpecs =
                buildVerifySpecsFromAnyShape((Map<String, Object>) verifyMap, context);

        page.verify(verifySpecs);
    }
    // ---------------------------------------
    // Minimal VerifySpec Builder
    // ---------------------------------------

    private Map<String, VerifySpec> buildVerifySpecsFromAnyShape(
            Map<String, Object> raw,
            RuntimeContext context
    ) {

        Map<String, VerifySpec> result = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : raw.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                continue;
            }

            // Case 1: Simple string → TEXT_EQUALS
            if (value instanceof String strVal) {

                result.put(key,
                        new VerifySpec(
                                ValidationType.TEXT_EQUALS,
                                strVal
                        ));
            }

            // Case 2: Boolean → IS_SELECTED
            else if (value instanceof Boolean boolVal) {

                result.put(key,
                        new VerifySpec(
                                ValidationType.IS_SELECTED,
                                boolVal
                        ));
            }

            // Case 3: Already structured map
            else if (value instanceof Map<?, ?> mapVal) {

                Object typeObj = mapVal.get("type");
                Object expectedObj = mapVal.get("expectedValue");

                if (typeObj == null) {
                    continue;
                }

                ValidationType type =
                        ValidationType.valueOf(String.valueOf(typeObj));

                result.put(key,
                        new VerifySpec(type, expectedObj));
            }
        }

        return result;
    }


    public void executeAction(
            BasePage page,
            Map<String, Object> rawTestData,
            RuntimeContext context
    ) {

        if (rawTestData == null || rawTestData.isEmpty()) {
            throw new IllegalArgumentException("TestData is empty");
        }

        for (Map.Entry<String, Object> entry : rawTestData.entrySet()) {

            String key = entry.getKey();

            if (RESERVED_NAMESPACES.contains(key)) {
                continue; // skip tables/components/etc for now
            }

            Object value = entry.getValue();

            // Only handle ACTION type instructions
            if (value instanceof String actionName) {
                page.performAction(actionName);
            }
        }
    }
}