package rajib.automation.framework.tests.debug;

import rajib.automation.framework.loader.JsonTestDataLoader;
import rajib.automation.framework.loader.TestDataLoader;
import rajib.automation.framework.normalization.DefaultTestDataNormalizer;
import rajib.automation.framework.normalization.TestDataNormalizer;
import rajib.automation.framework.resolution.DefaultIntentResolver;
import rajib.automation.framework.resolution.IntentResolver;
import rajib.automation.framework.resolution.ResolvedIntent;
import rajib.automation.framework.intent.Intent;

import java.util.Map;

public class TestDataPipelineDebugRunner {

    public static void main(String[] args) {

        // ---------- Inputs ----------
        String pageName = "DemoFormPage";
        String testCaseName = "Validation Test";

        // ---------- Step 1: Load JSON ----------
        TestDataLoader loader = new JsonTestDataLoader();
        Map<String, Object> rawTestData =
                loader.load(pageName, testCaseName);

        System.out.println("=== RAW TEST DATA ===");
        rawTestData.forEach((k, v) ->
                System.out.println(k + " -> " + v)
        );

        // ---------- Step 2: Normalize ----------
        TestDataNormalizer normalizer = new DefaultTestDataNormalizer();
        Map<String, Intent> intents =
                normalizer.normalize(rawTestData);

        System.out.println("\n=== NORMALIZED INTENTS ===");
        intents.forEach((k, v) ->
                System.out.println(
                        k
                                + " | populate=" + v.populate()
                                + " | verify=" + v.verify()
                )
        );

        // ---------- Step 3: Resolve ----------
        IntentResolver resolver = new DefaultIntentResolver();
        Map<String, ResolvedIntent> resolved =
                resolver.resolve(intents);

        System.out.println("\n=== RESOLVED INTENTS ===");
        resolved.forEach((k, r) ->
                System.out.println(
                        k
                                + " | shouldPopulate=" + r.shouldPopulate()
                                + " | shouldVerify=" + r.shouldVerify()
                )
        );
    }
}
