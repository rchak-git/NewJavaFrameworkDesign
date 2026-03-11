package rajib.automation.framework.normalization;

import rajib.automation.framework.execution.IntentExecutionPlan;
import rajib.automation.framework.execution.LegacyExecutionPlan;
import rajib.automation.framework.execution.CompositeExecutionPlan;
import rajib.automation.framework.intent.Intent;

import java.util.Map;

public class TestDataNormalizationRouter {

    private final DefaultTestDataNormalizer legacyNormalizer;
    private final ExecutionIntentNormalizer legacyExecutionNormalizer;
    private final DefaultTestDataNormalizerV11 compositeNormalizer;

    public TestDataNormalizationRouter(
            DefaultTestDataNormalizer legacyNormalizer,
            ExecutionIntentNormalizer legacyExecutionNormalizer,
            DefaultTestDataNormalizerV11 compositeNormalizer
    ) {
        this.legacyNormalizer = legacyNormalizer;
        this.legacyExecutionNormalizer = legacyExecutionNormalizer;
        this.compositeNormalizer = compositeNormalizer;
    }

    public IntentExecutionPlan normalize(
            String testDataId,
            Map<String, Object> rawTestData
    ) {

        if (isExplicitComposite(rawTestData)) {
            return new CompositeExecutionPlan(
                    compositeNormalizer.normalize(testDataId, rawTestData)
            );
        }

        Map<String, Intent> intents =
                legacyNormalizer.normalize(rawTestData);

        return new LegacyExecutionPlan(
                legacyExecutionNormalizer.normalize(intents)
        );
    }

    private boolean isExplicitComposite(Map<String, Object> data) {
        return data.containsKey("populate")
                || data.containsKey("verify")
                || data.containsKey("action");
    }
}
