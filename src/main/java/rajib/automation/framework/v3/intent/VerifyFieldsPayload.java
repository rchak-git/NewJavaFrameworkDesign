package rajib.automation.framework.v3.intent;



import rajib.automation.framework.intent.VerifySpec;

import java.util.Map;

public class VerifyFieldsPayload {

    private final Map<String, VerifySpec> fields;

    public VerifyFieldsPayload(Map<String, VerifySpec> fields) {
        this.fields = fields;
    }

    public Map<String, VerifySpec> fields() {
        return fields;
    }
}
