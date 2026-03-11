package rajib.automation.framework.normalization;



import rajib.automation.framework.intent.Intent;
import rajib.automation.framework.normalization.legacy.NormalizedIntent;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/*
 DEPRECATED - This class is no longer used in the current framework design. It was part of an earlier approach to normalizing execution intents, but has since been replaced by a more streamlined process that directly utilizes ResolvedIntents without an intermediate NormalizedIntent step.
 */
public class ExecutionIntentNormalizer {

    public List<NormalizedIntent> normalize(Map<String, Intent> fieldIntents) {

        List<NormalizedIntent> result = new ArrayList<>();

        for (Map.Entry<String, Intent> entry : fieldIntents.entrySet()) {

            String fieldKey = entry.getKey();
            Intent intent = entry.getValue();

            intent.populate().ifPresent(value ->
                    result.add(
                            NormalizedIntent.populate(fieldKey, value)
                    )
            );

            intent.verify().ifPresent(spec ->
                    result.add(
                            NormalizedIntent.verify(fieldKey, spec)
                    )
            );
        }

        return result;
    }
}
