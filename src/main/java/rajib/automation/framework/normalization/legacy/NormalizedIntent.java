package rajib.automation.framework.normalization.legacy;


import rajib.automation.framework.intent.IntentType;
import rajib.automation.framework.intent.VerifySpec;

public class NormalizedIntent {

    private final String fieldKey;
    private final IntentType intentType;
    private final Object value;
    private final VerifySpec verifySpec;

    private NormalizedIntent(
            String fieldKey,
            IntentType intentType,
            Object value,
            VerifySpec verifySpec
    ) {
        this.fieldKey = fieldKey;
        this.intentType = intentType;
        this.value = value;
        this.verifySpec = verifySpec;
    }

    // ---- factory methods ----

    public static NormalizedIntent populate(String fieldKey, Object value) {
        return new NormalizedIntent(
                fieldKey,
                IntentType.POPULATE,
                value,
                null
        );
    }

    public static NormalizedIntent verify(String fieldKey, VerifySpec spec) {
        return new NormalizedIntent(
                fieldKey,
                IntentType.VERIFY,
                spec.expectedValue(),
                spec
        );
    }

    // ---- getters ----

    public String getFieldKey() {
        return fieldKey;
    }

    public IntentType getIntentType() {
        return intentType;
    }

    public Object getValue() {
        return value;
    }

    public VerifySpec getVerifySpec() {
        return verifySpec;
    }
}
