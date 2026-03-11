package rajib.automation.framework.normalization;

import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.intent.Intent;
import rajib.automation.framework.intent.IntentType;
import rajib.automation.framework.intent.VerifySpec;
import rajib.automation.framework.normalization.model.NormalizedIntent;
import rajib.automation.framework.normalization.model.PopulateFieldsPayload;
import rajib.automation.framework.normalization.model.VerifyFieldsPayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultTestDataNormalizerV11 implements TestDataNormalizer {

    /**
     * V1.1 EXECUTION-LEVEL normalization
     * (This does NOT override TestDataNormalizer.normalize)
     */
    public List<NormalizedIntent> normalize(
            String testDataId,
            Map<String, Object> rawTestData
    ) {

        if (rawTestData == null || rawTestData.isEmpty()) {
            throw new IllegalArgumentException(
                    "TestData '" + testDataId + "' is empty"
            );
        }

        validateForbiddenKeys(rawTestData, testDataId);

        if (isExplicitComposite(rawTestData)) {
            return normalizeExplicitComposite(rawTestData, testDataId);
        }

        return normalizeCompositeShorthand(rawTestData, testDataId);
    }

    // ----------------------------------------------------------------------

    private boolean isExplicitComposite(Map<String, Object> data) {
        return data.containsKey("populate")
                || data.containsKey("verify")
                || data.containsKey("action");
    }

    // ---------------- N1.1 shorthand ----------------

    private List<NormalizedIntent> normalizeCompositeShorthand(
            Map<String, Object> data,
            String testDataId
    ) {

        Map<String, Object> populateFields = new HashMap<>();
        Map<String, VerifySpec> verifyFields = new HashMap<>();

        for (Map.Entry<String, Object> e : data.entrySet()) {

            String fieldKey = e.getKey();
            Object rawValue = e.getValue();

            if (rawValue == null) {
                throw new IllegalArgumentException(
                        "TestData '" + testDataId +
                                "' has null value for field: " + fieldKey
                );
            }

            if (!isPrimitiveOrBooleanString(rawValue)) {
                throw new IllegalArgumentException(
                        "Composite shorthand TestData '" + testDataId +
                                "' only supports primitive values. Field '" +
                                fieldKey + "' is: " +
                                rawValue.getClass().getSimpleName() +
                                ". Use explicit { populate / verify } shape."
                );
            }

            Object normalizedValue = normalizeBooleanString(rawValue);

            populateFields.put(fieldKey, normalizedValue);

            verifyFields.put(
                    fieldKey,
                    new VerifySpec(
                            ValidationType.TEXT_EQUALS,
                            normalizedValue,
                            null,
                            null
                    )
            );
        }

        List<NormalizedIntent> intents = new ArrayList<>();

        intents.add(
                NormalizedIntent.populate(
                        new PopulateFieldsPayload(populateFields)
                )
        );

        intents.add(
                NormalizedIntent.verify(
                        new VerifyFieldsPayload(verifyFields)
                )
        );

        return intents;
    }

    // ---------------- helpers ----------------

    private boolean isPrimitiveOrBooleanString(Object value) {
        if (value instanceof String) return true;
        return value instanceof Number || value instanceof Boolean;
    }

    private Object normalizeBooleanString(Object value) {
        if (value instanceof String s) {
            String t = s.trim();
            if ("true".equalsIgnoreCase(t)) return Boolean.TRUE;
            if ("false".equalsIgnoreCase(t)) return Boolean.FALSE;
            return s;
        }
        return value;
    }

    private void validateForbiddenKeys(
            Map<String, Object> data,
            String testDataId
    ) {
        for (String key : data.keySet()) {
            if ("phase".equalsIgnoreCase(key)
                    || "when".equalsIgnoreCase(key)) {
                throw new IllegalArgumentException(
                        "TestData '" + testDataId +
                                "' uses forbidden key '" + key +
                                "'. v1.1 forbids phase/timing in TestData."
                );
            }
        }
    }

    // ---------------- stubs ----------------

    private List<NormalizedIntent> normalizeExplicitComposite(
            Map<String, Object> data,
            String testDataId
    ) {
        throw new UnsupportedOperationException(
                "Not implemented yet (next step)"
        );
    }

    // ----------------------------------------------------------------------
    // Required by TestDataNormalizer (field-level normalization)
    // ----------------------------------------------------------------------

    @Override
    public Map<String, Intent> normalize(Map<String, Object> rawTestData) {
        // V1.1 does not participate in field-level normalization
        throw new UnsupportedOperationException(
                "Use DefaultTestDataNormalizer for field normalization"
        );
    }
}
