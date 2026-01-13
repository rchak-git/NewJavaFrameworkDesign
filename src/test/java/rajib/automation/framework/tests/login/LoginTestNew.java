package rajib.automation.framework.tests.login;

import agent.login.LoginAgentExecutor;
import agent.login.LoginContext;
import core.context.StepContext;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.codegen.loader.PageSchemaLoader;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.codegen.schema.PageSchema;
import rajib.automation.framework.codegen.schema.TableSchema;
import rajib.automation.framework.enums.ExecutionPhase;
import rajib.automation.framework.intent.Intent;
import rajib.automation.framework.intent.VerifySpec;
import rajib.automation.framework.loader.JsonTestDataLoader;
import rajib.automation.framework.model.TestData;
import rajib.automation.framework.normalization.DefaultTestDataNormalizer;
import rajib.automation.framework.pages.DemoFormPage;
import rajib.automation.framework.pages.PracticeFormPage;
import rajib.automation.framework.pages.TablesPage;
import rajib.automation.framework.pages.login.LoginPageManual;
import rajib.automation.framework.resolution.DefaultIntentResolver;
import rajib.automation.framework.resolution.IntentResolver;
import rajib.automation.framework.resolution.ResolvedIntent;
import rajib.automation.framework.steps.Steps;
import rajib.automation.framework.steps.TableActions;
import rajib.automation.framework.tables.actions.TableActionExecutor;
import rajib.automation.framework.tables.reader.TableReader;
import rajib.automation.framework.tables.verifier.TableVerifier;
import rajib.automation.framework.utils.TestDataLoader;
import reporting.ExtentTestManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Listeners(reporting.ExtentTestListener.class)
public class LoginTestNew extends BaseTest {

    @Test
    public void shouldPopulateAndVerifyDemoFormBeforeSubmit() {

        StepContext ctx = new StepContext(
                driver,
                "DemoForm Populate + Verify Test",
                ExtentTestManager.getTest()
        );

        driver.get(
                "file:///C:/Users/rchak/OneDrive/Documents/RajibWork/Learning/NewStudyNotes/AgenticAI/demo-form.html"
        );

        DemoFormPage page = new DemoFormPage();

        // -------------------------------------------------
        // 1️⃣ Load raw JSON test data (NO Steps.run return)
        // -------------------------------------------------
        Map<String, Object> rawData =
                new JsonTestDataLoader()
                        .load("DemoFormPage", "Validation Test");

        Steps.run(ctx, "Load raw test data", () -> {
            // logging only
        });

        // -------------------------------------------------
        // 2️⃣ Normalize → Intent
        // -------------------------------------------------
        Map<String, Intent> intents =
                new DefaultTestDataNormalizer()
                        .normalize(rawData);

        Steps.run(ctx, "Normalize test data to intents", () -> {
            // logging only
        });

        // -------------------------------------------------
        // 3️⃣ Resolve → ResolvedIntent
        // -------------------------------------------------
        Map<String, ResolvedIntent> resolvedIntents =
                new DefaultIntentResolver()
                        .resolve(intents);

        Steps.run(ctx, "Resolve intents", () -> {
            // logging only
        });

        Steps.run(ctx, "Enter Field values", () -> {
            page.execute(resolvedIntents, ExecutionPhase.POPULATE);
        });

        Steps.run(ctx, "Populate state-city composite", () -> {
            page.populateComposite("stateCity", resolvedIntents);
        });


        // -------------------------------------------------
        // 4️⃣ Execute resolved intents
        // -------------------------------------------------
        Steps.run(ctx, "Verify Field Values", () -> {
            page.execute(resolvedIntents, ExecutionPhase.VERIFY);
        });

        // -------------------------------------------------
        // 5️⃣ Submit form
        // -------------------------------------------------
        Steps.run(ctx, "Submit form", () -> {
            page.performAction("submit");
        });

        System.out.println("Wait Here");
    }




    @Test
    public void debugTableReaderAndVerifier() {

      //  WebDriver driver = DriverFactory.getDriver(); // or however you obtain it
        driver.get(
                "file:///C:/Users/rchak/OneDrive/Documents/RajibWork/Learning/NewStudyNotes/AgenticAI/demo-form.html"
        );


        DemoFormPage page = new DemoFormPage();  // initSchemas() already runs
        System.out.println("DEBUG usersTable present: " + page.table("usersTable").isPresent());

         TableReader reader =  page.tableReader("usersTable");

        int rows = reader.getRowCount();
        int cols = reader.getColumnCount();

        String statusRow0 = reader.getCellText(0, "Status");

        TableVerifier verifier = new TableVerifier(reader);
        verifier.hasAtLeastRows(1);
        verifier.cellEquals(1, "Status", "INACTIVE");

        List<Map<String, String>> allRows = page.tableReader("usersGrid").getAllRows();

        page.tableActions("usersGrid")
                .clickCell(0, "Status");

        page.tableActions("usersTable")
                .clickCell(1, "Name");

        page.tableActions("usersGrid")
                .clickInCell(
                        0,
                        "Status",
                        new LocatorSchema("css", "a")
                );

        TableSchema tableSchema = page.getTable("usersGrid");
        TableActionExecutor exec =
                new TableActionExecutor(driver, tableSchema);

        exec.isTablePresent();
        exec.getRowCount();
        exec.getCellValue(0, "Name");
        exec.getRowAsMap(0);




        System.out.println("Wait Here");

    }





}
