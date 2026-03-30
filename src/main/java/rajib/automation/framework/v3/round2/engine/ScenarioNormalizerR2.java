package rajib.automation.framework.v3.round2.engine;



import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.enums.ControlAction;

import java.util.*;

public class ScenarioNormalizerR2 {

    @SuppressWarnings("unchecked")
    public List<ControlCommand> normalize(
            Map<String, Object> testData,
            String scenarioName
    ) {

        List<ControlCommand> commands = new ArrayList<>();

        // 1️⃣ Extract sections
        Map<String, Object> dataSection =
                (Map<String, Object>) testData.get("data");

        Map<String, Object> scenarios =
                (Map<String, Object>) testData.get("scenarios");

        if (dataSection == null || scenarios == null) {
            throw new IllegalArgumentException("Invalid testData: missing 'data' or 'scenarios'");
        }

        // 2️⃣ Get selected scenario
        Map<String, Object> scenario =
                (Map<String, Object>) scenarios.get(scenarioName);

        if (scenario == null) {
            throw new IllegalArgumentException(
                    "Scenario not found: " + scenarioName
            );
        }

        // 3️⃣ Resolve refData
        String refDataKey = (String) scenario.get("refData");

        Map<String, Object> resolvedData =
                (Map<String, Object>) dataSection.get(refDataKey);

        if (resolvedData == null) {
            throw new IllegalArgumentException(
                    "Data not found for refData: " + refDataKey
            );
        }

        // =========================================================
        // 4️⃣ HANDLE POPULATE
        // =========================================================
        List<String> populateFields =
                (List<String>) scenario.get("populate");

        if (populateFields != null) {
            for (String field : populateFields) {

                Object value = resolvedData.get(field);

                if (value == null) {
                    throw new IllegalArgumentException(
                            "Field '" + field + "' not found in data '" + refDataKey + "'"
                    );
                }

                ControlCommand command = new ControlCommand(
                        ControlAction.POPULATE,
                        field,
                        value
                );

                commands.add(command);
            }
        }

        // =========================================================
        // 5️⃣ HANDLE VERIFY
        // =========================================================
        Map<String, Object> verifySection =
                (Map<String, Object>) scenario.get("verify");

        if (verifySection != null) {

            for (Map.Entry<String, Object> entry : verifySection.entrySet()) {

                String field = entry.getKey();

                Map<String, Object> verifyConfig =
                        (Map<String, Object>) entry.getValue();

                String typeStr = (String) verifyConfig.get("type");

                ValidationType validationType =
                        ValidationType.valueOf(typeStr);

                // 🔥 NEW: Support expected override
                Object expectedValue = verifyConfig.get("expected");

                Object value;

                if (expectedValue != null) {
                    value = expectedValue;
                } else {
                    value = resolvedData.get(field);

                    if (value == null) {
                        throw new IllegalArgumentException(
                                "Field '" + field + "' not found in data '" + refDataKey + "'"
                        );
                    }
                }

                ControlCommand command = new ControlCommand(
                        ControlAction.VERIFY,
                        field,
                        value,
                        validationType
                );

                commands.add(command);
            }
        }

        return commands;
    }

}