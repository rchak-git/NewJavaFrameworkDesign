package rajib.automation.framework.v3.loader;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class TestDataLoaderV3 {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public static Map<String, Object> load(String pageName, String testDataId) {

        String resourcePath = "testdata/" + pageName + ".json";

        try (InputStream is = TestDataLoaderV3.class
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

            Map<String, Object> td =
                    MAPPER.convertValue(testDataNode, Map.class);

            return resolveDataRef(rootNode, td, new HashSet<>(), resourcePath);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load TestData for page '" + pageName +
                            "', TestData ID '" + testDataId + "'",
                    e
            );
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> resolveDataRef(
            JsonNode rootNode,
            Map<String, Object> td,
            Set<String> visited,
            String resourcePath
    ) {

        Object refObj = td.get("dataref");

        if (!(refObj instanceof String refKey)) {
            return td; // no dataref → return as-is
        }

        if (!visited.add(refKey)) {
            throw new RuntimeException("Circular dataref detected: " + visited);
        }

        JsonNode refNode = rootNode.get(refKey);
        if (refNode == null) {
            throw new RuntimeException(
                    "dataref '" + refKey + "' not found in " + resourcePath
            );
        }

        Map<String, Object> refMap =
                MAPPER.convertValue(refNode, Map.class);

        // Recursively resolve nested datarefs if any
        refMap = resolveDataRef(rootNode, refMap, visited, resourcePath);

        Map<String, Object> assembled = new LinkedHashMap<>();

        // 🔹 Canonical assembly rule:
        // Referenced data becomes populate block
        assembled.put("populate", refMap);

        // Preserve action block if present
        if (td.containsKey("action")) {
            assembled.put("action", td.get("action"));
        }

        // Preserve verify block if explicitly present
        if (td.containsKey("verify")) {
            assembled.put("verify", td.get("verify"));
        }

        return assembled;
    }
}
