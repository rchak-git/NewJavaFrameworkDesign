package rajib.automation.framework.planning;


import rajib.automation.framework.td.model.Phase;
import rajib.automation.framework.routing.RoutingResult;

import java.util.List;
import java.util.Map;

public record ExecutionPlan(
        Map<Phase, List<RoutingResult>> phaseBuckets
) {

    public List<RoutingResult> getPhase(Phase phase) {
        return phaseBuckets.getOrDefault(phase, List.of());
    }
}
