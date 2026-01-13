package rajib.automation.framework.resolution;

import rajib.automation.framework.intent.Intent;

import java.util.HashMap;
import java.util.Map;

public class DefaultIntentResolver implements IntentResolver {

    @Override
    public Map<String, ResolvedIntent> resolve(Map<String, Intent> intents) {

        Map<String, ResolvedIntent> resolved = new HashMap<>();

        if (intents == null || intents.isEmpty()) {
            return resolved;
        }

        for (Map.Entry<String, Intent> entry : intents.entrySet()) {

            String fieldKey = entry.getKey();
            Intent intent = entry.getValue();

            boolean shouldPopulate = intent.populate().isPresent();
            boolean shouldVerify = intent.verify().isPresent();

            // Explicit skip case
            if (!shouldPopulate && !shouldVerify) {
                continue;
            }

            resolved.put(
                    fieldKey,
                    new ResolvedIntent(shouldPopulate, shouldVerify, intent)
            );
        }

        return resolved;
    }
}
