package rajib.automation.framework.tests.TestsRound2;

import core.context.StepContext;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.context.RuntimeContextHolder;
import rajib.automation.framework.v3.round2.testdatamodels.TestStepData;
import rajib.automation.framework.v3.round2.utils.StepConverters;
import rajib.automation.framework.v3.round2.engine.CommandDispatcherR2;
import rajib.automation.framework.v3.round2.loaders.TestDataLoaderR2;
import rajib.automation.framework.v3.round2.page.PracticeFormPage;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.testdatamodels.TestScenario;
import reporting.ExtentTestManager;

import java.util.List;


public class PracticeFormPageTest extends BaseTest {


/*
    @Test
    void testPracticeForm_validFormSubmission() {
        RuntimeContext context = RuntimeContextHolder.get();
        TestScenario scenario = TestDataLoaderR2.loadScenario("testdata/round2Data/practiceformtestdata.json", "valid_form_submission");
        driver.get("https://demoqa.com/automation-practice-form");
        List<ControlCommand> commands = scenario.steps().stream()
                .map(StepConverters::fromTestStepData)
                .toList();
        PracticeFormPage formPage = new PracticeFormPage(new ElementResolver(),context);
        CommandDispatcherR2.executeAll(formPage, commands);
        System.out.println("Wait Here");
    }

 */


    @Test
    void testPracticeForm_validFormSubmissionExtent() {
        RuntimeContext context = RuntimeContextHolder.get();

        driver.get("https://demoqa.com/automation-practice-form");
        List<TestStepData> steps = TestDataLoaderR2.loadResolvedTestSteps(
                "testdata/round2Data/practiceformtestdata.json",
                "Test1_PopulateFourFieldsAndSubmit"
        );
        // Create your context and include Extent node
        StepContext stepContext = new StepContext(
                driver,
                "testPracticeForm_validFormSubmissionExtent",
                ExtentTestManager.getTest()
        );

        List<ControlCommand> commands = steps.stream()
                .map(StepConverters::fromTestStepData)
                .toList();

        PracticeFormPage formPage = new PracticeFormPage(new ElementResolver(), context);

        // If your command dispatcher can accept a context, pass it in:
        CommandDispatcherR2.executeAll(formPage, commands, stepContext);

        System.out.println("Wait Here");
    }



  /*
  @Test
    public void testPracticeForm_validFormSubmission() {
        // Instantiate page with resolver
        PracticeFormPage formPage = new PracticeFormPage(new ElementResolver());

        // Load scenario data
        TestScenario scenario = TestDataLoaderR2.loadScenario("practice_form_testdata.json", "valid_form_submission");

        for (TestStepData step : scenario.steps()) {
            String fieldKey = step.fieldKey();
            String intent = step.intent();

            switch (intent) {
                case "populate" -> {
                    formPage.populateField(fieldKey, step.value());
                }
                case "verify" -> {
                    Object actual = formPage.readField(fieldKey);
                    Object expected = step.expected();
                    String validationType = step.validationType();

                    switch (validationType) {
                        case "TEXT_EQUALS" -> Assert.assertEquals(
                                actual,
                                expected,
                                "TEXT_EQUALS failed for field '" + fieldKey + "': expected [" + expected + "], actual [" + actual + "]"
                        );
                        case "TEXT_CONTAINS" -> Assert.assertTrue(
                                actual != null && actual.toString().contains(expected.toString()),
                                "TEXT_CONTAINS failed for field '" + fieldKey + "': expected to contain [" + expected + "], actual [" + actual + "]"
                        );
                        default -> Assert.fail("Unsupported validationType '" + validationType + "' for field '" + fieldKey + "'");
                    }
                }
                default -> Assert.fail("Unsupported intent '" + intent + "' for field '" + fieldKey + "'");
            }
        }
    }

   */

    /*
    @Test
    public void testPracticeFormBasicWiring() {
        // You may need to provide an actual ElementResolver or a suitable mock if not integrating with a browser
        ElementResolver resolver = new ElementResolver();

        PracticeFormPage page = new PracticeFormPage(resolver);

        // --- Hard-coded test data ---
        String firstNameValue = "Alice";
        String lastNameValue = "Smith";
         driver.get("https://demoqa.com/automation-practice-form");
        // Populate "firstName" field
        page.getControl("firstName").populate(
                new ControlCommand(ControlAction.POPULATE, "firstName", firstNameValue)
        );
        // Populate "lastName" field
        page.getControl("lastName").populate(
                new ControlCommand(ControlAction.POPULATE, "lastName", lastNameValue)
        );

        // Read back values (assuming .read() gets you the "value" attribute of the input)
        String firstNameActual = (String) page.getControl("firstName").read();
        String lastNameActual  = (String) page.getControl("lastName").read();

        Assert.assertEquals(firstNameActual, firstNameValue);
        Assert.assertEquals(lastNameActual, lastNameValue);

        // Optionally, verify (TEXT_EQUALS is typical)
        page.getControl("firstName").verify(
                new ControlCommand(ControlAction.VERIFY, "firstName", firstNameValue, ValidationType.TEXT_EQUALS)
        );
        page.getControl("lastName").verify(
                new ControlCommand(ControlAction.VERIFY, "lastName", lastNameValue, ValidationType.TEXT_EQUALS)
        );
    }

     */
}