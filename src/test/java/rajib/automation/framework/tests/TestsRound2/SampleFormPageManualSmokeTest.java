
/*
package rajib.automation.framework.tests.TestsRound2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// Framework imports
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.v3.round2.engine.ScenarioNormalizerR2;
import rajib.automation.framework.v3.round2.loaders.TestDataLoaderR2;
//import rajib.automation.framework.v3.round2.page.SampleFormPage;
import rajib.automation.framework.v3.round2.factory.ControlFactory;
import rajib.automation.framework.v3.round2.engine.CommandDispatcherR2;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.enums.ControlAction;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class SampleFormPageManualSmokeTest  {
    private WebDriver driver;
    private SampleFormPage page;

    @BeforeClass
    public void setup() {
        DriverFactory.setDriver("chrome");
       driver = DriverFactory.getDriver();
        ElementResolver resolver = new ElementResolver(driver);    // <-- Fix is here
        ControlFactory factory = new ControlFactory(resolver);
        page = new SampleFormPage(factory);
    }

    @Test
    public void testManualCommandDispatch() throws Exception {
        // 1. Load the sample form HTML page
        driver.get("file:///C:/Users/rchak/OneDrive/Documents/RajibWork/Learning/TestFrameworkRedesign/sample_form.html");

        // 2. Hand-craft a list of ControlCommands (the "escape hatch")
        List<ControlCommand> commands = List.of(
                new ControlCommand(ControlAction.POPULATE, "firstName", "Alice")
            //    new ControlCommand(ControlAction.POPULATE, "sports", true),
             //   new ControlCommand(ControlAction.ADD, "placesDualListBox", List.of("Milan")),
             //   new ControlCommand(ControlAction.ADD, "placesDualListBox", List.of("Danville")),
              //  new ControlCommand(ControlAction.REMOVE, "placesDualListBox", List.of("Danville"))
        );

        // 3. Dispatch these commands to the page's controls via the dispatcher
        CommandDispatcherR2.executeAll(page, commands);
         System.out.println("Wait Here");
        // Optionally: add asserts or verification steps here, as your Controls/framework support
    }



    @Test
    public void testDataDrivenScenario_AddAndInvalidRemove() throws Exception {
        // 1. Setup WebDriver and page

        try {
            driver.get("file:///C:/Users/rchak/OneDrive/Documents/RajibWork/Learning/TestFrameworkRedesign/sample_form.html");

          /*  // 2. Load JSON scenario data
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> testData = mapper.readValue(
                    Files.newInputStream(Paths.get("src/test/resources/testdata/round2Data/sample_form_test_data.json")),
                    Map.class
            );

           */
/*
            Map<String, Object> testData = TestDataLoaderR2.load("testdata/round2Data/sample_form_test_data.json");
            // 3. Pick the scenario you want to run
            Map<String, Object> scenarios = (Map<String, Object>) testData.get("scenarios");
            Map<String, Object> scenario = (Map<String, Object>) scenarios.get("AddAndInvalidRemove");

            // 4. Normalize scenario into commands
            ScenarioNormalizerR2 normalizer = new ScenarioNormalizerR2();
            List<ControlCommand> commands = normalizer.normalizeFlatScenario(scenario);

            // 5. Dispatch
            // Assume you have the right ElementResolver/ControlFactory/Page setup already
            CommandDispatcherR2.executeAll(page, commands);

            // 6. Add asserts as needed (e.g., check error flagged for remove-ABCD, etc)
        } finally {
            driver.quit();
        }
    }





    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
*/