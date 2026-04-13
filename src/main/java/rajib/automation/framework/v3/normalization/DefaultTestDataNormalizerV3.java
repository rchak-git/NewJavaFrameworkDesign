
/*
Deprecated: This class is no longer used in the framework. It was an initial implementation of a TestData normalizer for V3, but the normalization logic has since been moved to a more flexible and extensible system. This class is retained for reference purposes only and should not be used in new code.
 TODO: Consider deleting this class in the future to reduce confusion and maintenance overhead.
 */



package rajib.automation.framework.v3.normalization;

import rajib.automation.framework.enums.ValidationType;


import rajib.automation.framework.v3.intent.ActionPayload;
import rajib.automation.framework.v3.intent.NormalizedIntent;
import rajib.automation.framework.v3.intent.PopulateFieldsPayload;
import rajib.automation.framework.v3.intent.VerifyFieldsPayload;
import rajib.automation.framework.intent.VerifySpec;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultTestDataNormalizerV3  {

    /**
     * V3 Normalization Rules
     *
     * 1. If "populate" key exists → populate only
     * 2. If "verify" key exists → verify only
     * 3. If both exist → generate both explicitly
     * 4. If neither exist → shorthand mode:
     *      - populate intent
     *      - verify intent (TEXT_EQUALS implicit)
     */
    public List<NormalizedIntent> normalize(
            Map<String, Object> rawTestData
    ) {

        if (rawTestData == null || rawTestData.isEmpty()) {
            throw new IllegalArgumentException(
                    "TestData is empty"
            );
        }

        List<NormalizedIntent> intents = new ArrayList<>();

        boolean hasPopulate = rawTestData.containsKey("populate");
        boolean hasVerify = rawTestData.containsKey("verify");

        if (hasPopulate || hasVerify) {
            normalizeExplicitBlocks(rawTestData, intents);
        } else {
            normalizeShorthand(rawTestData, intents);
        }

        return intents;
    }

    // ---------------------------------------------------------
    // Explicit populate / verify block handling
    // ---------------------------------------------------------
    @SuppressWarnings("unchecked")
    private void normalizeExplicitBlocks(
            Map<String, Object> raw,
            List<NormalizedIntent> intents
    ) {

        // -----------------------------
        // 1️⃣ Populate block
        // -----------------------------
        if (raw.containsKey("populate")) {

            Object populateObj = raw.get("populate");

            if (!(populateObj instanceof Map<?, ?> populateMap)) {
                throw new IllegalArgumentException(
                        "'populate' block must be a Map"
                );
            }

            intents.add(
                    NormalizedIntent.populate(
                            new PopulateFieldsPayload(
                                    (Map<String, Object>) populateMap
                            )
                    )
            );
        }

// -----------------------------
// 3️⃣ Action block
// -----------------------------
        if (raw.containsKey("action")) {

            Object actionObj = raw.get("action");

            if (!(actionObj instanceof Map<?, ?> actionMap)) {
                throw new IllegalArgumentException(
                        "'action' block must be a Map"
                );
            }

            intents.add(
                    NormalizedIntent.action(
                            new ActionPayload((Map<String, Object>) actionMap)
                    )
            );
        }





        // -----------------------------
        // 2️⃣ Verify block
        // -----------------------------
        if (raw.containsKey("verify")) {

            Object verifyObj = raw.get("verify");

            if (!(verifyObj instanceof Map<?, ?> verifyMap)) {
                throw new IllegalArgumentException(
                        "'verify' block must be a Map"
                );
            }

            Map<String, VerifySpec> verifySpecs = new java.util.HashMap<>();

            for (Map.Entry<String, Object> entry :
                    ((Map<String, Object>) verifyMap).entrySet()) {

                String fieldKey = entry.getKey();
                Object value = entry.getValue();

                // -------------------------------------------------
                // 🔹 Map-based value
                // -------------------------------------------------
                if (value instanceof Map<?, ?> structured) {

                    // -------------------------------------------------
                    // If map does NOT contain "type" → treat as component block
                    // -------------------------------------------------
                    if (!structured.containsKey("type")) {

                        verifySpecs.put(
                                fieldKey,
                                new VerifySpec(
                                        ValidationType.TEXT_EQUALS, // dummy for now
                                        structured
                                )
                        );

                        continue;
                    }

                    // -------------------------------------------------
                    // Structured atomic verify
                    // -------------------------------------------------
                    ValidationType type =
                            ValidationType.valueOf(structured.get("type").toString());

                    Object expectedValue =
                            structured.get("expectedValue");

                    verifySpecs.put(
                            fieldKey,
                            new VerifySpec(type, expectedValue)
                    );

                } else {

                    // -------------------------------------------------
                    // Simple atomic verify → implicit TEXT_EQUALS
                    // -------------------------------------------------
                    verifySpecs.put(
                            fieldKey,
                            new VerifySpec(
                                    ValidationType.TEXT_EQUALS,
                                    value
                            )
                    );
                }
            }

            intents.add(
                    NormalizedIntent.verify(
                            new VerifyFieldsPayload(verifySpecs)
                    )
            );
        }
    }

    // ---------------------------------------------------------
    // Shorthand dual mode (populate + implicit verify)
    // ---------------------------------------------------------
    private void normalizeShorthand(
            Map<String, Object> raw,
            List<NormalizedIntent> intents
    ) {

        Map<String, Object> populateFields = new java.util.HashMap<>();
        Map<String, VerifySpec> verifyFields = new java.util.HashMap<>();

        for (Map.Entry<String, Object> entry : raw.entrySet()) {

            String fieldKey = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                throw new IllegalArgumentException(
                        "Shorthand TestData contains null for field: " + fieldKey
                );
            }

            // Populate intent
            populateFields.put(fieldKey, value);

            // Implicit verify intent (TEXT_EQUALS)
            verifyFields.put(
                    fieldKey,
                    new VerifySpec(
                            ValidationType.TEXT_EQUALS,
                            value
                    )
            );
        }

        // Add populate intent
        intents.add(
                NormalizedIntent.populate(
                        new PopulateFieldsPayload(populateFields)
                )
        );

        // Add verify intent
        intents.add(
                NormalizedIntent.verify(
                        new VerifyFieldsPayload(verifyFields)
                )
        );
    }


    // ---------------------------------------------------------
    // Required by TestDataNormalizer (field-level normalization)
    // ---------------------------------------------------------

}
