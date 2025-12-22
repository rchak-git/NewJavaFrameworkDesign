package rajib.automation.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import rajib.automation.framework.model.TestData;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TestDataLoader {

    public static TestData load(String pageName, String scenario) {

        try (InputStream is = TestDataLoader.class
                .getClassLoader()
                .getResourceAsStream("testdata/" + pageName + ".json")) {

            if (is == null) {
                throw new RuntimeException(
                        "Test data file not found on classpath: testdata/" + pageName + ".json");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(is);
            JsonNode scenarioNode = rootNode.get(scenario);

            if (scenarioNode == null) {
                throw new RuntimeException(
                        "Scenario '" + scenario + "' not found in " + pageName + ".json");
            }

            Map<String, String> data = new HashMap<>();
            scenarioNode.fields()
                    .forEachRemaining(e ->
                            data.put(e.getKey(), e.getValue().asText()));

            return new TestData(data);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load test data for page: " + pageName +
                            ", scenario: " + scenario, e);
        }
    }
}
