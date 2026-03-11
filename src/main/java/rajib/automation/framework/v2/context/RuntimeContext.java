package rajib.automation.framework.v2.context;

import java.util.HashMap;
import java.util.Map;

public class RuntimeContext {

    private final Map<String, Object> store = new HashMap<>();

    public void put(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        return store.get(key);
    }

    public String getString(String key) {
        Object value = store.get(key);
        return value != null ? value.toString() : null;
    }

    public boolean contains(String key) {
        return store.containsKey(key);
    }

    @Override
    public String toString() {
        return store.toString();
    }
}
