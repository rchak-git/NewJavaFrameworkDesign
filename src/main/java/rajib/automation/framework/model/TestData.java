package rajib.automation.framework.model;

import java.util.Map;

public class TestData {

    private final Map<String, String> data;

    public TestData(Map<String, String> data) {
        this.data = data;
    }

    public String get(String key) {
        if (!data.containsKey(key)) {
            throw new RuntimeException("Test data missing key: " + key);
        }
        return data.get(key);
    }
}
