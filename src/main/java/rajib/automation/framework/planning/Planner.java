package rajib.automation.framework.planning;


import rajib.automation.framework.routing.RoutingResult;

import java.util.List;

public interface Planner {

    ExecutionPlan plan(List<RoutingResult> routingResults);
}
