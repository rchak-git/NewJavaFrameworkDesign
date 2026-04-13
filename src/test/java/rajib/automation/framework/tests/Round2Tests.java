package rajib.automation.framework.tests;

import org.testng.annotations.Test;
import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.engine.ScenarioNormalizerR2;
import rajib.automation.framework.v3.round2.enums.ControlAction;
import rajib.automation.framework.v3.round2.factory.ControlFactory;
import rajib.automation.framework.v3.round2.loaders.TestDataLoaderR2;
import rajib.automation.framework.v3.round2.page.BasePageR2;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import java.util.Map;
import java.util.List;

public class Round2Tests {

  /*  @Test
    public void regressionSmokeTest() {
        // ✅ Step 1: Start browser
        DriverFactory.setDriver("chrome");

        DriverFactory.getDriver().get("https://demoqa.com/automation-practice-form");

        Map<String, Object> testData =
                TestDataLoaderR2.load("testdata/round2Data/refactor_new_enums.json");

        ScenarioNormalizerR2 normalizer = new ScenarioNormalizerR2();

        // New: Extract just the "createUser" scenario to pass to flat normalizer
        Map<String, Object> allScenarios = (Map<String, Object>) testData.get("scenarios");
        Map<String, Object> scenario = (Map<String, Object>) allScenarios.get("HappyPath");

        List<ControlCommand> commands =
                normalizer.normalizeFlatScenario(scenario);

        System.out.println("=== Commands Generated ===");
        commands.forEach(System.out::println);

        ElementResolver resolver = new ElementResolver();
        ControlFactory controlFactory = new ControlFactory(resolver);

        List<FieldSchema> fieldSchemas = List.of(
                // ✅ Use the new FieldSchema factory method for simple controls
                FieldSchema.ofSimple(
                        "firstName",
                        FieldType.TEXTBOX,
                        "First Name",
                        new LocatorSchema("id", "firstName")
                ),
                FieldSchema.ofSimple(
                        "sports",
                        FieldType.CHECKBOX,
                        "Sports Hobby",
                        new LocatorSchema("xpath", "//input[@id='hobbies-checkbox-1']")
                )
        );

        BasePageR2 page = new BasePageR2(fieldSchemas, controlFactory);

        System.out.println("=== Execution Started ===");
        try {
            page.execute(commands);
        }
        catch(Exception e)
        {
            e.printStackTrace(); // (It's helpful to print, at least during dev)
        }

        DriverFactory.quitDriver();
    }

   */
    /*
    @Test
    public void testDualListBoxCustomDemo() {
        DriverFactory.setDriver("chrome");
        DriverFactory.getDriver().get("https://www.testmuai.com/selenium-playground/bootstrap-dual-list-box-demo/");

        ElementResolver resolver = new ElementResolver();
        ControlFactory controlFactory = new ControlFactory(resolver);

        FieldSchema dualListSchema = new FieldSchema(
                "duallist",
                FieldType.DUALLISTBOX,
                "Places",
                Map.of(
                        "source",      new LocatorSchema("css", ".dual-list.list-left ul.list-group"),
                        "target",     new LocatorSchema("css", ".dual-list.list-right ul.list-group"),
                        "addButton",      new LocatorSchema("css", "button.move-right"),
                        "removeButton",   new LocatorSchema("css", "button.move-left"),
                        "leftSelectAll",  new LocatorSchema("css", ".dual-list.list-left .listcheck .chectbtn"),
                        "rightSelectAll", new LocatorSchema("css", ".dual-list.list-right .listcheck .chectbtn")
                )
        );

        List<FieldSchema> schemas = List.of(dualListSchema);
        BasePageR2 page = new BasePageR2(schemas, controlFactory);

        List<ControlCommand> commands = List.of(
                new ControlCommand(ControlAction.ADD, "duallist", List.of("Danville")),
                new ControlCommand(ControlAction.REMOVE, "duallist", List.of("Grange"))
             //   new ControlCommand(ControlAction.ADD_ALL, "duallist", null),   // Simulate: select-all left, add
             //   new ControlCommand(ControlAction.REMOVE_ALL, "duallist", null) // Simulate: select-all right, remove
        );

        try {
            page.execute(commands);
        }
        catch(Exception e)
        {

        }


        DriverFactory.quitDriver();
    }

     */
}