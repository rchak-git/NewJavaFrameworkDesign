package rajib.automation.framework.v3.round2.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import rajib.automation.framework.v3.round2.testdatamodels.TestStepData;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class TestDataLoaderR2 {

    private static final ObjectMapper mapper = new ObjectMapper();

    // Scenario definitions indexed by scenarioId (populate when file is loaded)
    private static final Map<String, List<TestStepData>> scenarioMap = new HashMap<>();

    // Loads all JSON objects (scenario defs and test cases) in order from the file.
    private static List<Map<String, Object>> loadAllEntries(String fileName) {
        try (InputStream is = TestDataLoaderR2.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) throw new RuntimeException("Test data file not found: " + fileName);
            return mapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    private static Map<String, Object> normalizeStepMap(Map<String, Object> stepMap) {
        return new LinkedHashMap<>(stepMap);
    }

    // Index all scenario definitions in the file (call this ONCE before running tests).
    private static void indexScenarios(String fileName) {
        scenarioMap.clear();
        for (Map<String, Object> entry : loadAllEntries(fileName)) {
            if (entry.containsKey("scenarioId")) {
                String scenarioId = (String) entry.get("scenarioId");
                List<Map<String, Object>> rawSteps = (List<Map<String, Object>>) entry.get("steps");
                List<TestStepData> steps = rawSteps.stream()
                        .map(TestDataLoaderR2::normalizeStepMap)
                        .map(stepMap -> mapper.convertValue(stepMap, TestStepData.class))
                        .collect(Collectors.toList());
                scenarioMap.put(scenarioId, steps);
            }
        }
    }

    // Loads and expands one test case (by testName) with all included scenario steps and parameter substitution.
    public static List<TestStepData> loadResolvedTestSteps(String fileName, String testName) {
        indexScenarios(fileName);

        for (Map<String, Object> entry : loadAllEntries(fileName)) {
            if (testName.equals(entry.get("testName"))) {
                List<TestStepData> resolvedSteps = new ArrayList<>();

                List<Object> scenarioList = (List<Object>) entry.get("scenario");

                for (Object obj : scenarioList) {
                    Map<String, Object> stepMap = (Map<String, Object>) obj;

                    if (stepMap.containsKey("use")) {
                        String useScenarioId = (String) stepMap.get("use");
                        Map<String, Object> parameters = stepMap.get("parameters") != null
                                ? (Map<String, Object>) stepMap.get("parameters")
                                : Collections.emptyMap();

                        List<TestStepData> includedSteps = scenarioMap.get(useScenarioId);
                        if (includedSteps == null) {
                            throw new RuntimeException("ScenarioId not found: " + useScenarioId);
                        }

                        for (TestStepData incStep : includedSteps) {
                            if (shouldIncludeStep(incStep, parameters)) {
                                resolvedSteps.add(substituteParams(incStep, parameters));
                            }
                        }
                    } else {
                        TestStepData directStep = mapper.convertValue(normalizeStepMap(stepMap), TestStepData.class);
                        resolvedSteps.add(directStep);
                    }
                }
                return resolvedSteps;
            }
        }
        throw new RuntimeException("Test case not found: " + testName);
    }

    // Helper: Only include the step if all placeholders are present in the data
    private static boolean shouldIncludeStep(TestStepData step, Map<String, Object> parameters) {
        return containsOnlyResolvablePlaceholders(step.value(), parameters)
                && containsOnlyResolvablePlaceholders(step.expected(), parameters)
                && containsOnlyResolvablePlaceholders(step.matchBy(), parameters);
    }

    private static boolean containsOnlyResolvablePlaceholders(Object field, Map<String, Object> parameters) {
        if (field == null) return true;

        if (field instanceof String str) {
            if (isPlaceholder(str)) {
                String key = extractPlaceholderName(str);
                return parameters.containsKey(key);
            }
            return true;
        }

        if (field instanceof Map<?, ?> map) {
            for (Object value : map.values()) {
                if (!containsOnlyResolvablePlaceholders(value, parameters)) {
                    return false;
                }
            }
            return true;
        }

        if (field instanceof List<?> list) {
            for (Object item : list) {
                if (!containsOnlyResolvablePlaceholders(item, parameters)) {
                    return false;
                }
            }
            return true;
        }

        return true;
    }

    private static boolean isPlaceholder(String str) {
        return str != null && str.startsWith("${") && str.endsWith("}") && str.length() > 3;
    }

    private static String extractPlaceholderName(String str) {
        return str.substring(2, str.length() - 1);
    }

    public static TestStepData substituteParams(TestStepData step, Map<String, Object> params) {
        return new TestStepData(
                step.fieldKey(),
                step.intent(),
                substituteField(step.value(), params),
                substituteField(step.expected(), params),
                substituteField(step.matchBy(), params),
                step.validationType(),
                step.actionType(),
                step.populationType()
        );
    }

    private static Object substituteField(Object field, Map<String, Object> params) {
        if (field == null || params == null) return field;

        if (field instanceof String str) {
            String result = str;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String placeholder = "${" + entry.getKey() + "}";
                Object value = entry.getValue();
                String replacement;
                if (value instanceof List<?> list) {
                    replacement = list.stream().map(String::valueOf).collect(Collectors.joining(","));
                } else if (value != null) {
                    replacement = value.toString();
                } else {
                    replacement = "";
                }
                result = result.replace(placeholder, replacement);
            }
            return result;
        }

        if (field instanceof Map<?, ?> map) {
            Map<String, Object> resolved = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                resolved.put(String.valueOf(entry.getKey()), substituteField(entry.getValue(), params));
            }
            return resolved;
        }

        if (field instanceof List<?> list) {
            return list.stream()
                    .map(elem -> substituteField(elem, params))
                    .toList();
        }

        return field;
    }
}