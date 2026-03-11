package rajib.automation.framework.model;

import rajib.automation.framework.enums.TargetType;
import rajib.automation.framework.intent.VerifySpec;

import java.util.Map;

public record ComponentVerifySpec(
        TargetType targetType,
        String target,
        Map<String, Object> match,
        Map<String, VerifySpec> rules
) {}
