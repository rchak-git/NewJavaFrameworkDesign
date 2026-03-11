package rajib.automation.framework.execution;

import rajib.automation.framework.normalization.model.NormalizedIntent;
import rajib.automation.framework.planning.ExecutionPlan;

import java.util.List;

public class CompositeExecutionPlan implements IntentExecutionPlan {

    private final List<NormalizedIntent> intents;

    public CompositeExecutionPlan(List<NormalizedIntent> intents) {
        this.intents = intents;
    }

    public List<NormalizedIntent> getIntents() {
        return intents;
    }
}
