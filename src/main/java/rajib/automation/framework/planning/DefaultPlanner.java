package rajib.automation.framework.planning;


import rajib.automation.framework.routing.RoutingResult;
import rajib.automation.framework.td.model.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import rajib.automation.framework.intent.VerifySpec;

/*
Deprecated - This class is no longer used in the current framework design. It was part of an earlier approach to planning execution based on RoutingResults, but has since been replaced by a more streamlined process that directly utilizes ResolvedIntents without an intermediate RoutingResult step.
 */
public class DefaultPlanner implements Planner {

    @Override
    public ExecutionPlan plan(List<RoutingResult> routingResults) {

        Map<Phase, List<RoutingResult>> buckets =
                new EnumMap<>(Phase.class);

        for (Phase phase : Phase.values()) {
            buckets.put(phase, new ArrayList<>());
        }

        if (routingResults == null) {
            return new ExecutionPlan(buckets);
        }

        for (RoutingResult result : routingResults) {

            Phase phase = determinePhase(result);
            buckets.get(phase).add(result);
        }

        return new ExecutionPlan(buckets);
    }

    private Phase determinePhase(RoutingResult result) {

        ExecutionTarget target = result.target();
        Object payload = result.payload();

        // 1️⃣ ESCAPE → MAIN
        if (target == ExecutionTarget.ESCAPE) {
            return Phase.MAIN;
        }

        // 2️⃣ PAGE
        if (target == ExecutionTarget.PAGE && payload instanceof PageNode page) {
            if (page.verify() != null) {
                return phaseFromVerify(page.verify());
            }
            return Phase.MAIN;
        }

        // 3️⃣ FIELD
        if (target == ExecutionTarget.FIELD && payload instanceof FieldNode field) {

            if (field.verify() != null) {
                return phaseFromVerify(field.verify());
            }

            return Phase.MAIN;
        }

        // 4️⃣ TABLE
        if (target == ExecutionTarget.TABLE && payload instanceof TableNode table) {

            if (table.verify() != null) {
                return phaseFromVerify(table.verify());
            }

            return Phase.MAIN;
        }

        // Safe default
        return Phase.MAIN;
    }

    private Phase phaseFromVerify(VerifySpec verify) {
        return verify.effectivePhase() == VerifyPhaseHint.PRE
                ? Phase.PRE
                : Phase.POST;
    }
}
