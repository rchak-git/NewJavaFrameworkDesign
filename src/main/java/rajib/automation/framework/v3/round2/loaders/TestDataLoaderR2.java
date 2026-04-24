package rajib.automation.framework.v3.round2.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import rajib.automation.framework.v3.round2.testdatamodels.TestStepData;
public class TestDataLoaderR2 {

    private static final ObjectMapper mapper = new ObjectMapper();

    // Scenario definitions indexed by scenarioId (populate when file is loaded)
    private static Map<String, List<TestStepData>> scenarioMap = new HashMap<>();

    // Loads all JSON objects (scenario defs and test cases) in order from the file.
    private static List<Map<String, Object>> loadAllEntries(String fileName) {
        try (InputStream is = TestDataLoaderR2.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) throw new RuntimeException("Test data file not found: " + fileName);
            return mapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    // Index all scenario definitions in the file (call this ONCE before running tests).
    private static void indexScenarios(String fileName) {
        scenarioMap.clear();
        for (Map<String, Object> entry : loadAllEntries(fileName)) {
            if (entry.containsKey("scenarioId")) {
                String scenarioId = (String) entry.get("scenarioId");
                List<Map<String, Object>> rawSteps = (List<Map<String, Object>>) entry.get("steps");
                List<TestStepData> steps = rawSteps.stream()
                        .map(stepMap -> mapper.convertValue(stepMap, TestStepData.class))
                        .collect(Collectors.toList());
                scenarioMap.put(scenarioId, steps);
            }
        }
    }

    // Loads and expands one test case (by testName) with all included scenario steps and parameter substitution.
    public static List<TestStepData> loadResolvedTestSteps(String fileName, String testName) {
        // First, index scenarios
        indexScenarios(fileName);

        for (Map<String, Object> entry : loadAllEntries(fileName)) {
            if (testName.equals(entry.get("testName"))) {
                List<TestStepData> resolvedSteps = new ArrayList<>();

                List<Object> scenarioList = (List<Object>) entry.get("scenario"); // Each step/inclusion is a Map here

                for (Object obj : scenarioList) {
                    Map<String, Object> stepMap = (Map<String, Object>) obj;
                    if (stepMap.containsKey("use")) {
                        // This is a scenario inclusion
                        String useScenarioId = (String) stepMap.get("use");
                        Map<String, String> parameters = stepMap.get("parameters") != null
                                ? (Map<String, String>) stepMap.get("parameters")
                                : Collections.emptyMap();
                        List<TestStepData> includedSteps = scenarioMap.get(useScenarioId);
                        if (includedSteps == null)
                            throw new RuntimeException("ScenarioId not found: " + useScenarioId);
                        for (TestStepData incStep : includedSteps) {
                            // Only include this step if: (A) It's not a parameterized value/expected, or (B) if the referenced parameter is present
                            if (shouldIncludeStep(incStep, parameters)) {
                                resolvedSteps.add(substituteParams(incStep, parameters));
                            }
                        }
                    } else {
                        // This is a manual, hardcoded step
                        TestStepData directStep = mapper.convertValue(stepMap, TestStepData.class);
                        resolvedSteps.add(directStep);
                    }
                }
                return resolvedSteps;
            }
        }
        throw new RuntimeException("Test case not found: " + testName);
    }

    // Helper: Only include the step if all placeholders are present in the data
    private static boolean shouldIncludeStep(TestStepData step, Map<String, String> parameters) {
        // For value
        if (step.value() instanceof String valueStr && isPlaceholder(valueStr)) {
            String key = extractPlaceholderName(valueStr);
            return parameters.containsKey(key);
        }
        // For expected (you can extend this logic if you want to filter on expected fields too)
        if (step.expected() instanceof String expectedStr && isPlaceholder(expectedStr)) {
            String key = extractPlaceholderName(expectedStr);
            return parameters.containsKey(key);
        }
        // Not a parameterized step, always include (hardcoded)
        return true;
    }

    // Returns true if string is exactly "${param}"
    private static boolean isPlaceholder(String str) {
        return str != null && str.startsWith("${") && str.endsWith("}") && str.length() > 3;
    }

    // Extracts the param name from "${param}"
    private static String extractPlaceholderName(String str) {
        return str.substring(2, str.length() - 1);
    }
    // Substitute all parameter occurrences in a TestStepData
    public static TestStepData substituteParams(TestStepData step, Map<String, String> params) {
        return new TestStepData(
                step.fieldKey(),
                step.intent(),
                substituteField(step.value(), params),
                substituteField(step.expected(), params),
                step.validationType(),
                step.actionType()
        );
    }

    // Replace ${var} in an Object (usually String) with params.get(var)
    private static Object substituteField(Object field, Map<String, String> params) {
        if (field == null || params == null) return field;
        if (field instanceof String str) {
            String result = str;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String placeholder = "${" + entry.getKey() + "}";
                result = result.replace(placeholder, entry.getValue() != null ? entry.getValue() : "");
            }
            return result;
        }
        // If your value types might be arrays/maps etc., you could recursively implement those here too
        return field;
    }
}