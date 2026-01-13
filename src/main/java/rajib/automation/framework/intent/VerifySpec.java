package rajib.automation.framework.intent;

import rajib.automation.framework.enums.ValidationType;

public record VerifySpec(
        ValidationType type,
        Object expectedValue
) {}
