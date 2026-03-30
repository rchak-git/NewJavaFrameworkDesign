package rajib.automation.framework.v3.round2.loaders;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class TestDataLoaderR2 {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> load(String fileName) {
        try {
            InputStream is = TestDataLoaderR2.class
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            if (is == null) {
                throw new RuntimeException("Test data file not found: " + fileName);
            }

            return mapper.readValue(is, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }
}