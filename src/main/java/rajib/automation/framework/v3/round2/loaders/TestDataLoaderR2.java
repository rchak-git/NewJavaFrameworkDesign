package rajib.automation.framework.v3.round2.loaders;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import rajib.automation.framework.v3.round2.testdatamodels.*;

public class TestDataLoaderR2 {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<TestScenario> loadAllScenarios(String fileName) {
        try (InputStream is = TestDataLoaderR2.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) throw new RuntimeException("Test data file not found: " + fileName);
            return mapper.readValue(is, new TypeReference<List<TestScenario>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    public static TestScenario loadScenario(String fileName, String scenarioName) {
        List<TestScenario> scenarios = loadAllScenarios(fileName);
        Optional<TestScenario> result = scenarios.stream()
                .filter(s -> s.scenario().equals(scenarioName))
                .findFirst();
        return result.orElseThrow(() ->
                new RuntimeException("Scenario not found: " + scenarioName));
    }
}