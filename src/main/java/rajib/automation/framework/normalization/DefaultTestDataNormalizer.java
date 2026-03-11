package rajib.automation.framework.normalization;


import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.intent.Intent;
import rajib.automation.framework.intent.VerifySpec;
import rajib.automation.framework.td.model.VerifyPhaseHint;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
DEPRECATED: Will be deleted
 */
public class DefaultTestDataNormalizer implements TestDataNormalizer {

    @Override
    public Map<String, Intent> normalize(Map<String, Object> rawTestData) {

        Map<String, Intent> normalized = new HashMap<>();

        if (rawTestData == null) {
            return normalized;
        }

        for (Map.Entry<String, Object> entry : rawTestData.entrySet()) {

            String fieldKey = entry.getKey();
            Object value = entry.getValue();

            Intent intent = normalizeValue(value);
            normalized.put(fieldKey, intent);
        }

        return normalized;
    }

    private Intent normalizeValue(Object value) {

        // Case 1️⃣ Primitive → populate + verify (default EQUALS)
        if (isPrimitive(value)) {
            return new Intent(
                    Optional.of(value),
                    Optional.of(new VerifySpec(
                            ValidationType.TEXT_EQUALS,
                            value,
                            null,
                            null
                    ))
            );
        }

        // Case 2️⃣ Map → explicit intent
        if (value instanceof Map<?, ?> mapValue) {
            return normalizeIntentMap(mapValue);
        }

        throw new IllegalArgumentException(
                "Unsupported test data value type: " + value
        );
    }

    private Intent normalizeIntentMap(Map<?, ?> mapValue) {

        Object populate = mapValue.get("populate");
        Object verify = mapValue.get("verify");

        Optional<Object> populateOpt =
                populate != null ? Optional.of(populate) : Optional.empty();

        Optional<VerifySpec> verifyOpt = Optional.empty();

        if (verify != null) {

            // Case 1️⃣: verify is primitive → default EQUALS
            if (isPrimitive(verify)) {
                verifyOpt = Optional.of(
                        new VerifySpec(
                                ValidationType.TEXT_EQUALS,
                                verify,
                                null,
                                null
                        )
                );
            }

            // Case 2️⃣: verify is object → explicit validation
            else if (verify instanceof Map<?, ?> verifyMap) {

                Object typeObj = verifyMap.get("type");
                Object valueObj = verifyMap.get("value");

                if (valueObj == null) {
                    throw new IllegalArgumentException(
                            "Verify object must contain 'value'"
                    );
                }

                ValidationType validationType =
                        typeObj != null
                                ? ValidationType.valueOf(typeObj.toString())
                                : ValidationType.TEXT_EQUALS;

                verifyOpt = Optional.of(
                        new VerifySpec(
                                validationType,
                                valueObj,
                                null,
                                null
                        )
                );
            }

            else {
                throw new IllegalArgumentException(
                        "Unsupported verify format: " + verify
                );
            }
        }

        if (populateOpt.isEmpty() && verifyOpt.isEmpty()) {
            throw new IllegalArgumentException(
                    "TestData object must contain 'populate' and/or 'verify'"
            );
        }

        return new Intent(populateOpt, verifyOpt);
    }

    private boolean isPrimitive(Object value) {
        return value instanceof String
                || value instanceof Number
                || value instanceof Boolean;
    }
}
