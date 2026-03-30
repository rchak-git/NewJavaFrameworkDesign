package rajib.automation.framework.intent;

import rajib.automation.framework.enums.ExecutionPhase;
import rajib.automation.framework.execution.LegacyExecutionPlan;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.model.TestData;
import rajib.automation.framework.normalization.DefaultTestDataNormalizer;
import rajib.automation.framework.normalization.ExecutionIntentNormalizer;
import rajib.automation.framework.normalization.legacy.NormalizedIntent;
import rajib.automation.framework.pages.RegistrationPage;
import rajib.automation.framework.planning.DefaultPlanner;
import rajib.automation.framework.planning.ExecutionPlan;
import rajib.automation.framework.resolution.ResolvedIntent;
import rajib.automation.framework.routing.DefaultRouter;
import rajib.automation.framework.routing.RoutingResult;
import rajib.automation.framework.td.model.ExecutionTarget;
import rajib.automation.framework.td.model.FieldNode;
import rajib.automation.framework.td.model.Phase;
import rajib.automation.framework.utils.TestDataLoader;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/*
 DEPRECATED: This class represents the legacy intent execution flow, which is tightly coupled to the existing BasePage logic and test data structure. It serves as a transitional adapter to bridge the gap between the new intent-based design and the old event-based execution model. The goal is to eventually migrate all test cases to use the new intent-based approach directly, at which point this class can be removed.
 */





public class IntentDispatcher {

    public void execute(String intentName) {
        execute(intentName, null);
    }

    public void execute(String intentName, String profileName) {

        System.out.println("Executing intent: " + intentName);

        switch (intentName) {
            case "REGISTER_USER":
                executeRegisterUser(profileName);
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown intent: " + intentName
                );
        }
    }

    private void executeRegisterUser(String profileName) {

        RegistrationPage page = ensureRegistrationPage();

        if (profileName == null || profileName.isBlank()) {
            throw new IllegalArgumentException(
                    "REGISTER_USER requires a profile name"
            );
        }

        // 1️⃣ Load legacy test data (Map<String, String>)
        TestData testData = TestDataLoader.load("RegistrationPage", profileName);

        if (testData == null || testData.isEmpty()) {
            throw new IllegalStateException(
                    "No test data found for profile: " + profileName
            );
        }

        // 2️⃣ Adapter: Map<String, String> → Map<String, Object>
        Map<String, Object> rawData = new LinkedHashMap<>();
        testData.getData().forEach(rawData::put);

        // 3️⃣ Normalize TestData → field-level Intents
        DefaultTestDataNormalizer testDataNormalizer =
                new DefaultTestDataNormalizer();

        Map<String, Intent> fieldIntents =
                testDataNormalizer.normalize(rawData);

        // 4️⃣ Convert field-intents → legacy execution events (event-based)
        ExecutionIntentNormalizer executionIntentNormalizer =
                new ExecutionIntentNormalizer();

        List<rajib.automation.framework.normalization.legacy.NormalizedIntent> legacyIntents =
                executionIntentNormalizer.normalize(fieldIntents);

        // 5️⃣ Wrap into legacy execution plan
        LegacyExecutionPlan legacyExecutionPlan =
                new LegacyExecutionPlan(legacyIntents);

        // 6️⃣ Route execution intents
        DefaultRouter router = new DefaultRouter();
        List<rajib.automation.framework.routing.RoutingResult> routingResults =
                router.route(legacyExecutionPlan);

        // 7️⃣ Plan phases (PRE / MAIN / POST)
        DefaultPlanner planner = new DefaultPlanner();
        rajib.automation.framework.planning.ExecutionPlan executionPlan =
                planner.plan(routingResults);

        // 8️⃣ LEGACY ADAPTER
        // Legacy happy-path semantics:
        // - Populate ALL fields first
        // - Ignore phase (verify defaults push everything to POST)
        Map<String, ResolvedIntent> resolvedIntents = new LinkedHashMap<>();

        java.util.function.Consumer<rajib.automation.framework.routing.RoutingResult> collectPopulate =
                rr -> {
                    if (rr.target() == ExecutionTarget.FIELD
                            && rr.payload() instanceof FieldNode field
                            && field.populate() != null) {

                        Intent intent = new Intent(
                                Optional.of(field.populate()),
                                Optional.ofNullable(field.verify())
                        );

                        resolvedIntents.put(
                                rr.targetKey(),
                                new ResolvedIntent(
                                        true,   // shouldPopulate
                                        false,  // legacy: verification not executed here
                                        intent
                                )
                        );
                    }
                };

        // Legacy populate ignores phase
        executionPlan.getPhase(Phase.PRE).forEach(collectPopulate);
        executionPlan.getPhase(Phase.MAIN).forEach(collectPopulate);
        executionPlan.getPhase(Phase.POST).forEach(collectPopulate);

        // 9️⃣ Execute using existing BasePage logic
        page.execute(resolvedIntents, ExecutionPhase.POPULATE);

        // 🔹 TEMP STEP 2: Verify populated values before submit
        page.execute(resolvedIntents, ExecutionPhase.VERIFY);
        // 🔟 Submit (unchanged)
        page.performAction("submit");
    }


    private RegistrationPage ensureRegistrationPage() {
        DriverFactory.getDriver()
                .get("https://demoqa.com/automation-practice-form");
        return new RegistrationPage();
    }
}
