package rajib.automation.framework.v3.table.model;

import rajib.automation.framework.intent.VerifySpec;

import java.util.Map;

public record RowVerifySpec(
        Map<String, Object> match,
        Map<String, VerifySpec> verify
) {}