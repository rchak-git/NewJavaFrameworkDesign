package rajib.automation.framework.tests.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.context.StepContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.pages.DemoFormPage;
import rajib.automation.framework.steps.Steps;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v3.execution.ExecutionEngineV3;
import reporting.ExtentTestManager;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Listeners(reporting.ExtentTestListener.class)
public class LoginTestNewVersion extends BaseTest {


    // Test scenario:
// 1. Navigate to registration page
// 2. Register new user with dynamic email
// 3. Login using generated email
// 4. Verify product price for "ZARA COAT 3"
// 5. Add product to cart
// 6. Open cart
// 7. Verify subtotal and total are $11500


    @Test
    void testPopulateAndVerifyTable() throws Exception {

        StepContext ctx = new StepContext(
                driver,
                "registerAndLoginFlow_V3",
                ExtentTestManager.getTest()
        );
        driver.get("file:///C:/Users/rchak/OneDrive/Documents/RajibWork/Learning/NewStudyNotes/AgenticAI/demo-form.html");

        DemoFormPage page = new DemoFormPage();
        ExecutionEngineV3 executionEngine = new ExecutionEngineV3();
        RuntimeContext context = new RuntimeContext();

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> testData =
                mapper.readValue(
                        Files.newInputStream(
                                Paths.get("src/test/resources/testdata/demoFormTestData.json")
                        ),
                        Map.class
                );

        Map<String, Object> scenarioData =
                (Map<String, Object>) testData.get("TD_DemoForm_Populate_And_Verify_Table");
        Steps.run(ctx, "Populate values in Page", () -> {
            executionEngine.executePopulate(page, scenarioData, context);

        });
        Steps.run(ctx, "Verify the populated values in Page", () -> {
            executionEngine.executeVerify(page, scenarioData, context);

        });

    }





}
