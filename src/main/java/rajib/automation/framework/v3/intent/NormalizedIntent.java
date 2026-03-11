package rajib.automation.framework.v3.intent;

import rajib.automation.framework.intent.IntentType;

import java.util.Objects;

public class NormalizedIntent {

    private final IntentType intentType;
    private final Object payload;

    public NormalizedIntent(IntentType intentType, Object payload) {
        this.intentType = Objects.requireNonNull(intentType, "intentType cannot be null");
        this.payload = Objects.requireNonNull(payload, "payload cannot be null");
    }

    public IntentType getIntentType() {
        return intentType;
    }

    public Object getPayload() {
        return payload;
    }

    // ---------------------------------------------------------
    // Factory methods (used by Normalizer)
    // ---------------------------------------------------------

    public static NormalizedIntent populate(PopulateFieldsPayload payload) {
        return new NormalizedIntent(IntentType.POPULATE, payload);
    }

    public static NormalizedIntent verify(VerifyFieldsPayload payload) {
        return new NormalizedIntent(IntentType.VERIFY, payload);
    }

    public static NormalizedIntent action(ActionPayload payload) {
        return new NormalizedIntent(IntentType.ACTION, payload);
    }

    // ---------------------------------------------------------
    // Convenience helpers
    // ---------------------------------------------------------

    public boolean isPopulateIntent() {
        return intentType == IntentType.POPULATE;
    }

    public boolean isVerifyIntent() {
        return intentType == IntentType.VERIFY;
    }

    public boolean isActionIntent() {
        return intentType == IntentType.ACTION;
    }

    @Override
    public String toString() {
        return "NormalizedIntent{" +
                "intentType=" + intentType +
                ", payload=" + payload.getClass().getSimpleName() +
                '}';
    }
}
