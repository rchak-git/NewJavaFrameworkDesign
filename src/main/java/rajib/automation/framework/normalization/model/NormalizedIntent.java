package rajib.automation.framework.normalization.model;

import rajib.automation.framework.intent.IntentType;
import rajib.automation.framework.v3.intent.ActionPayload;

public class NormalizedIntent {

    private final IntentType intentType;
    private final Object payload;

    private NormalizedIntent(IntentType intentType, Object payload) {
        this.intentType = intentType;
        this.payload = payload;
    }

    // -------- Factory methods --------

    public static NormalizedIntent populate(PopulateFieldsPayload payload) {
        return new NormalizedIntent(IntentType.POPULATE, payload);
    }

    public static NormalizedIntent verify(VerifyFieldsPayload payload) {
        return new NormalizedIntent(IntentType.VERIFY, payload);
    }

    public static NormalizedIntent action(ActionPayload payload) {
        return new NormalizedIntent(IntentType.ACTION, payload);
    }

    // -------- Getters --------

    public IntentType getIntentType() {
        return intentType;
    }

    public Object getPayload() {
        return payload;
    }
}
