package rajib.automation.framework.routing;

import rajib.automation.framework.execution.LegacyExecutionPlan;
import rajib.automation.framework.normalization.legacy.NormalizedIntent;
import rajib.automation.framework.intent.IntentType;
import rajib.automation.framework.td.model.ExecutionTarget;
import rajib.automation.framework.td.model.FieldNode;

import java.util.*;

/**
 * Routes legacy (implicit / happy-path) intents into RoutingResults.
 *
 * Merges POPULATE + VERIFY intents per fieldKey into a single FieldNode.
 */

/*
    DEPRECATED - This class is no longer used in the current framework design. It was part of an earlier approach to routing execution plans based on NormalizedIntents, but has since been replaced by a more streamlined process that directly utilizes ResolvedIntents without an intermediate NormalizedIntent step.

 */
public class DefaultRouter {

    public List<RoutingResult> route(LegacyExecutionPlan plan) {

        Map<String, Object> populateByField = new HashMap<>();
        Map<String, Object> verifyByField = new HashMap<>();

        // 1️⃣ Collect intents by type
        for (NormalizedIntent intent : plan.getIntents()) {

            String fieldKey = intent.getFieldKey();

            if (intent.getIntentType() == IntentType.POPULATE) {
                populateByField.put(fieldKey, intent.getValue());
            }

            if (intent.getIntentType() == IntentType.VERIFY) {
                verifyByField.put(fieldKey, intent.getVerifySpec());
            }
        }

        // 2️⃣ Build RoutingResults
        List<RoutingResult> results = new ArrayList<>();

        for (String fieldKey : unionKeys(populateByField, verifyByField)) {

            FieldNode fieldNode = new FieldNode(
                    populateByField.get(fieldKey),
                    (rajib.automation.framework.intent.VerifySpec) verifyByField.get(fieldKey),
                    null // legacy path has no explicit FieldAction
            );

            results.add(
                    new RoutingResult(
                            ExecutionTarget.FIELD,
                            fieldKey,
                            fieldNode
                    )
            );
        }

        return results;
    }

    private Set<String> unionKeys(Map<String, ?> a, Map<String, ?> b) {
        Set<String> keys = new HashSet<>(a.keySet());
        keys.addAll(b.keySet());
        return keys;
    }
}
