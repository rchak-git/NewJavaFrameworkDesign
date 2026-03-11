package rajib.automation.framework.normalization.model;


import rajib.automation.framework.intent.VerifySpec;

import java.util.Map;

public record VerifyFieldsPayload(
        Map<String, VerifySpec> fields
) {}
