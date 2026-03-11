package rajib.automation.framework.execution;

import rajib.automation.framework.normalization.legacy.NormalizedIntent;
import java.util.List;
/*
DEPRECATED - This class is no longer used in the current framework design. It was part of an earlier approach to representing execution plans using NormalizedIntents, but has since been replaced by a more streamlined process that directly utilizes ResolvedIntents without an intermediate NormalizedIntent step.
 */

public class LegacyExecutionPlan implements IntentExecutionPlan {

    private final List<NormalizedIntent> intents;

    public LegacyExecutionPlan(List<NormalizedIntent> intents) {
        this.intents = intents;
    }

    public List<NormalizedIntent> getIntents() {
        return intents;
    }
}
