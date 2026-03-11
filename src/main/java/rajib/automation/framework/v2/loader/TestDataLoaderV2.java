package rajib.automation.framework.v2.loader;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class TestDataLoaderV2 {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Loads a data-only TestData object for a given page and TestData ID.
     *
     * @param pageName   Page name (maps to testdata/<pageName>.json)
     * @param testDataId Identifier of the TestData object (e.g. TD_ValidLoginInput)
     * @return Map<String, Object> representing pure TestData
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> load(String pageName, String testDataId) {

        String resourcePath = "testdata/" + pageName + ".json";

        try (InputStream is = TestDataLoaderV2.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new RuntimeException(
                        "Test data file not found on classpath: " + resourcePath
                );
            }

            JsonNode rootNode = MAPPER.readTree(is);
            JsonNode testDataNode = rootNode.get(testDataId);

            if (testDataNode == null) {
                throw new RuntimeException(
                        "TestData ID '" + testDataId +
                                "' not found in " + resourcePath
                );
            }

            // Preserve structure exactly as authored (maps, lists, numbers, strings)
            return MAPPER.convertValue(testDataNode, Map.class);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load TestData for page '" + pageName +
                            "', TestData ID '" + testDataId + "'",
                    e
            );
        }
    }
}
