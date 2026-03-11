package rajib.automation.framework.v3.execution;

import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.intent.IntentType;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.loader.TestDataLoaderV2;
import rajib.automation.framework.v3.intent.NormalizedIntent;
import rajib.automation.framework.v3.intent.PopulateFieldsPayload;
import rajib.automation.framework.v3.intent.VerifyFieldsPayload;
import rajib.automation.framework.v3.normalization.DefaultTestDataNormalizerV3;


import java.util.List;
import java.util.Map;

public class TestDataExecutorV3 {

    private final DefaultTestDataNormalizerV3 normalizer =
            new DefaultTestDataNormalizerV3();

    public void execute(
            BasePage page,
            String testDataFile,
            String testDataId,
            RuntimeContext context
    ) {

        // 1️⃣ Load raw JSON
        Map<String, Object> raw =
                TestDataLoaderV2.load(testDataFile, testDataId);

        // 2️⃣ Normalize into V3 intents
        List<NormalizedIntent> intents =
                normalizer.normalize( raw);

        // 3️⃣ Execute in order
        for (NormalizedIntent intent : intents) {

            if (intent.getIntentType() == IntentType.POPULATE) {

                PopulateFieldsPayload payload =
                        (PopulateFieldsPayload) intent.getPayload();

                page.populate(payload.fields(), context);
            }

            else if (intent.getIntentType() == IntentType.VERIFY) {

                VerifyFieldsPayload payload =
                        (VerifyFieldsPayload) intent.getPayload();
                page.verify(payload.fields());
            }

            else {
                throw new IllegalStateException(
                        "Unsupported intent type: " + intent.getIntentType()
                );
            }
        }
    }


}
