package rajib.automation.framework.tests;

import org.openqa.selenium.WebElement;
import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.engine.ScenarioNormalizerR2;
import rajib.automation.framework.v3.round2.factory.ControlFactory;
import rajib.automation.framework.v3.round2.loaders.TestDataLoaderR2;
import rajib.automation.framework.v3.round2.page.BasePageR2;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Round2Tests {

    public static void main(String[] args)  {

        // ✅ Step 1: Start browser
        DriverFactory.setDriver("chrome");

        DriverFactory.getDriver().get("https://demoqa.com/automation-practice-form");

        Map<String, Object> testData =
                TestDataLoaderR2.load("testdata/Round2TestData.json");

        ScenarioNormalizerR2 normalizer = new ScenarioNormalizerR2();

        List<ControlCommand> commands =
                normalizer.normalize(testData, "createUser");

        System.out.println("=== Commands Generated ===");
        commands.forEach(System.out::println);

        ElementResolver resolver = new ElementResolver();
        ControlFactory controlFactory = new ControlFactory(resolver);

        List<FieldSchema> fieldSchemas = List.of(

                new FieldSchema(
                        "firstName",
                        FieldType.TEXTBOX,
                        "First Name",
                        new LocatorSchema("id", "firstName")
                ),

                new FieldSchema(
                        "sports",
                        FieldType.CHECKBOX,
                        "Sports Hobby",
                        new LocatorSchema("xpath", "//label[text()='Sports']")
                )
        );

        BasePageR2 page = new BasePageR2(fieldSchemas, controlFactory);

        System.out.println("=== Execution Started ===");

        page.execute(commands);

        DriverFactory.quitDriver();
    }
}

