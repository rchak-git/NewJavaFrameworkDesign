package rajib.automation.framework.v3.intent;

import java.util.Map;

public class PopulateFieldsPayload {

    private final Map<String, Object> fields;

    public PopulateFieldsPayload(Map<String, Object> fields) {
        this.fields = fields;
    }

    public Map<String, Object> fields() {
        return fields;
    }
}
