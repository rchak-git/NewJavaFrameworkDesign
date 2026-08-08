package rajib.automation.framework.intent;


import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.td.model.VerifyPhaseHint;

import java.util.Map;

public record VerifySpec(
        ValidationType type,
        Object expectedValue,
        Map<String, Object> params,
        VerifyPhaseHint phase
) {

    public VerifySpec {
        if (type == null) {
            throw new IllegalArgumentException("VerifySpec.type must not be null");
        }
    }

    // 🔹 MVP convenience constructor (type + expected)
    public VerifySpec(ValidationType type, Object expectedValue) {
        this(type, expectedValue, null, null);
    }

    // 🔹 MVP convenience constructor (type only, for state checks)
    public VerifySpec(ValidationType type) {
        this(type, null, null, null);
    }

    public VerifyPhaseHint effectivePhase() {
        return phase != null ? phase : VerifyPhaseHint.POST;
    }
}
