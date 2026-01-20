package rajib.automation.framework.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class JsonTestDataLoader implements TestDataLoader {

    private static final String TESTDATA_BASE_PATH = "testdata/";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> load(String pageName, String testCaseName) {

        String resourcePath = TESTDATA_BASE_PATH + pageName + ".json";

        try (InputStream is = JsonTestDataLoader.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new RuntimeException(
                        "Test data file not found on classpath: " + resourcePath
                );
            }

            JsonNode rootNode = objectMapper.readTree(is);

            JsonNode testCasesNode = rootNode.get("testCases");
            if (testCasesNode == null || !testCasesNode.isArray()) {
                throw new RuntimeException(
                        "Invalid test data format: 'testCases' array missing in "
                                + resourcePath
                );
            }

            for (JsonNode testCaseNode : testCasesNode) {

                JsonNode nameNode = testCaseNode.get("name");
                if (nameNode != null && testCaseName.equals(nameNode.asText())) {

                    JsonNode dataNode = testCaseNode.get("data");
                    if (dataNode == null || !dataNode.isObject()) {
                        throw new RuntimeException(
                                "Invalid test case format: 'data' object missing for test case: "
                                        + testCaseName
                        );
                    }

                    // Convert JSON object → Map<String, Object>
                    return objectMapper.convertValue(
                            dataNode,
                            new TypeReference<Map<String, Object>>() {}
                    );
                }
            }

            throw new RuntimeException(
                    "Test case '" + testCaseName + "' not found in " + resourcePath
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load test data for page: "
                            + pageName + ", test case: " + testCaseName,
                    e
            );
        }
    }

    public Map<String, Object> loadProfile(String pageName, String profileName) {

        String resourcePath = TESTDATA_BASE_PATH + pageName + ".json";

        try (InputStream is = JsonTestDataLoader.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new RuntimeException(
                        "Test data file not found on classpath: " + resourcePath
                );
            }

            JsonNode rootNode = objectMapper.readTree(is);

            JsonNode profilesNode = rootNode.get("profiles");
            if (profilesNode == null || !profilesNode.isObject()) {
                throw new RuntimeException(
                        "Invalid test data format: 'profiles' object missing in "
                                + resourcePath
                );
            }

            JsonNode profileNode = profilesNode.get(profileName);
            if (profileNode == null || !profileNode.isObject()) {
                throw new RuntimeException(
                        "Profile '" + profileName + "' not found in " + resourcePath
                );
            }

            // Convert JSON object → Map<String, Object>
            return objectMapper.convertValue(
                    profileNode,
                    new TypeReference<Map<String, Object>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load test data profile '"
                            + profileName + "' for page: " + pageName,
                    e
            );
        }
    }

}
