package rajib.automation.framework.intent;

import rajib.automation.framework.enums.ExecutionPhase;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.model.TestData;
import rajib.automation.framework.pages.RegistrationPage;
import rajib.automation.framework.resolution.ResolvedIntent;
import rajib.automation.framework.utils.TestDataLoader;

import rajib.automation.framework.factory.DriverFactory;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

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


        TestData testData = TestDataLoader.load("RegistrationPage", profileName);

        if (testData == null || testData.isEmpty()) {
            throw new IllegalStateException(
                    "No test data found for profile: " + profileName
            );
        }

        Map<String, ResolvedIntent> resolvedIntents = new LinkedHashMap<>();
        ResolvedIntent ri =null;
        for (Map.Entry<String, String> entry : testData.getData().entrySet()) {

            String fieldKey = entry.getKey();
            String value = entry.getValue();

            // ✅ Populate-only Intent
            Intent intent = new Intent(Optional.of(value), Optional.empty());

             ri = new ResolvedIntent(
                    true,   // shouldPopulate
                    false,  // shouldVerify
                    intent
            );

            resolvedIntents.put(fieldKey,new ResolvedIntent(
                    true,   // shouldPopulate
                    false,  // shouldVerify
                    intent
            ));
        }

        // ✅ Delegate to BasePage execution
        page.execute(resolvedIntents, ExecutionPhase.POPULATE);

        // To DO - HOW to populate the Composite FIelds ?

        // Submit (ACTION)
        page.performAction("submit");

    }

    private RegistrationPage ensureRegistrationPage() {
        DriverFactory.getDriver().get("https://demoqa.com/automation-practice-form");
        return new RegistrationPage();
    }

}
