package rajib.automation.framework.tests.Miscelleneous;

/*
====================================================
TEST DATA DSL CONTRACT - V3
====================================================

VALID ROOT KEYS:
- populate
- verify
- action
- dataref

VERIFY SHAPE:

{
  "verify": {
     "fieldKey": {
        "type": "TEXT_EQUALS",
        "expectedValue": "some value"
     }
  }
}

ACTION SHAPE:

{
  "action": {
     "submit": true
  }
}

DO NOT USE:
- "expected"
- "validation"
- flat verify without "type"
====================================================
*/

import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.pages.DemoFormPage;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v3.execution.ExecutionEngineV3;
import rajib.automation.framework.v3.loader.TestDataLoaderV3;

import java.util.Map;

public class FormDslExperimentTest extends BaseTest {

// Test scenario:
// 1. Navigate to demoqa practice form
// 2. Populate First Name, Last Name, Email
// 3. Select Gender as Male
// 4. Enter Mobile number
// 5. Select Hobby: Sports
// 6. Submit form
// 7. Verify submitted values are displayed correctly

    // File: src/test/java/rajib/automation/framework/tests/Miscelleneous/FormDslExperimentTest.java


        @Test
        public void demoQaPracticeForm_SubmitOnly() {
            RuntimeContext context = new RuntimeContext();
            ExecutionEngineV3 engine = new ExecutionEngineV3();

            // 1. Navigate to demoqa practice form
            driver.get("https://demoqa.com/automation-practice-form");

            // 2. Load test data
            Map<String, Object> td = TestDataLoaderV3.load("DemoQaPracticeFormTestData", "TC_DEMOQA_FORM_SUBMIT_ONLY");

            // 3. Create page object
            DemoFormPage page = new DemoFormPage();

            // 4. Execute populate and action steps
            engine.executePopulate(page, td, context);
            engine.executeAction(page, td, context);
            System.out.println(context);

        }



}