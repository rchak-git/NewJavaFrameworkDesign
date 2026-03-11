/*
package rajib.automation.framework.tests.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.context.StepContext;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.codegen.schema.ComponentSchema;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.codegen.schema.TableSchema;
import rajib.automation.framework.enums.ExecutionPhase;
import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.intent.Intent;
import rajib.automation.framework.intent.VerifySpec;
import rajib.automation.framework.loader.JsonTestDataLoader;

import rajib.automation.framework.v3.table.model.TableVerificationSpec;
import rajib.automation.framework.normalization.DefaultTestDataNormalizer;
import rajib.automation.framework.pages.DemoFormPage;


//import rajib.automation.framework.pages.rahul.DashboardPage;
import rajib.automation.framework.pages.rahul.DashboardPage;
import rajib.automation.framework.pages.rahul.LoginPage;
import rajib.automation.framework.pages.rahul.MycartsPage;
import rajib.automation.framework.pages.rahul.RegisterPage;
import rajib.automation.framework.resolution.DefaultIntentResolver;
import rajib.automation.framework.resolution.ResolvedIntent;
import rajib.automation.framework.steps.Steps;
import rajib.automation.framework.tables.actions.TableActionExecutor;
import rajib.automation.framework.tables.reader.TableReader;

import rajib.automation.framework.tables.verifier.TableVerifier;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.loader.TestDataLoaderV2;
import rajib.automation.framework.v3.execution.ExecutionEngineV3;
import rajib.automation.framework.v3.loader.TestDataLoaderV3;
import rajib.automation.framework.v3.table.runtime.TableVerifierV3;
import reporting.ExtentTestManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

@Listeners(reporting.ExtentTestListener.class)
public class LoginTestNew extends BaseTest {


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


   /*
    @Test
    public void registerAndLoginFlow_V3() {

        StepContext ctx = new StepContext(
                driver,
                "registerAndLoginFlow_V3",
                ExtentTestManager.getTest()
        );

        RuntimeContext context = new RuntimeContext();
        ExecutionEngineV3 engine = new ExecutionEngineV3();

        // -------------------------------------------------
        // STEP 1 – Register New User
        // -------------------------------------------------

        Steps.run(ctx, "Register New User", () -> {

            driver.get("https://www.rahulshettyacademy.com/client/#/auth/register");

            Map<String, Object> registerRaw =
                    TestDataLoaderV3.load("RegisterTestData", "TD_ENTER_REGISTRATION");

            RegisterPage registerPage = new RegisterPage();

            engine.executePopulate(registerPage, registerRaw, context);
            engine.executeAction(registerPage, registerRaw, context);
        });

        // -------------------------------------------------
        // STEP 2 – Login Using Generated Email
        // -------------------------------------------------

        Steps.run(ctx, "Login With Registered User", () -> {

            driver.get("https://www.rahulshettyacademy.com/client/#/auth/login");

            Map<String, Object> loginRaw =
                    TestDataLoaderV3.load("LoginTestData", "TD_LoginWithRegisteredUser");

            LoginPage loginPage = new LoginPage();

            engine.executePopulate(loginPage, loginRaw, context);
            engine.executeAction(loginPage, loginRaw, context);
        });

        // -------------------------------------------------
        // STEP 3 – Verify Dashboard
        // -------------------------------------------------

        Steps.run(ctx, "Verify Dashboard", () -> {

            Map<String, Object> verifyDashboard =
                    TestDataLoaderV3.load("DashboardTestData", "TD_VerifySingleProductPrice");

            engine.executeVerify(new DashboardPage(), verifyDashboard, context);
        });

        // -------------------------------------------------
        // STEP 4 – Add Product To Cart
        // -------------------------------------------------

        Steps.run(ctx, "Add Product To Cart", () -> {

            Map<String, Object> addProductRaw =
                    TestDataLoaderV3.load("DashboardTestData", "TD_VerifySingleProductPrice");

            DashboardPage dashboardPage = new DashboardPage();

            engine.executeAction(dashboardPage, addProductRaw, context);
        });

        // -------------------------------------------------
        // STEP 5 – Open Cart
        // -------------------------------------------------

        Steps.run(ctx, "Open Cart Page", () -> {

          /*  Map<String, Object> openCartRaw =
                    TestDataLoaderV3.load("DashboardTestData", "TD_OpenCart");

            DashboardPage dashboardPage = new DashboardPage();

            engine.executeAction(dashboardPage, openCartRaw, context);


            DashboardPage dashboardPage = new DashboardPage();

                dashboardPage.performComponentAction("headerNav", "cart");

        });

        // -------------------------------------------------
        // STEP 6 – Verify Cart Page
        // -------------------------------------------------

        Steps.run(ctx, "Verify Cart Page", () -> {

            Map<String, Object> verifyCart =
                    TestDataLoaderV3.load("MycartsTestData", "TD_VERIFY_CARTPAGE");

            engine.executeVerify(new MycartsPage(), verifyCart, context);
        });
    }


    @Test
    public void registerAndLoginFlow_V3_OldX() {

        StepContext ctx = new StepContext(
                driver,
                "registerAndLoginFlow_V3",
                ExtentTestManager.getTest()
        );

        RuntimeContext context = new RuntimeContext();
        ExecutionEngineV3 engine = new ExecutionEngineV3();

        // -------------------------------------------------
        // STEP 1 – Register New User (Dynamic Email)
        // -------------------------------------------------
         driver.get("https://www.rahulshettyacademy.com/client/#/auth/register");
        Steps.run(ctx, "Register New User", () -> {

            Map<String, Object> registerRaw =
                    TestDataLoaderV2.load("RegisterTestData", "TD_RegisterNewUser");
            RegisterPage registerPage = new RegisterPage();
            engine.executePopulate(registerPage, registerRaw, context);
            registerPage.performAction("login");

        });

        // -------------------------------------------------
        // STEP 2 – Login Using Context Email
        // -------------------------------------------------

        Steps.run(ctx, "Login With Registered User", () -> {
            driver.get("https://www.rahulshettyacademy.com/client/#/auth/login");
            Map<String, Object> loginRaw =
                    TestDataLoaderV2.load("LoginTestData", "TD_LoginWithRegisteredUser");
            LoginPage loginPage = new LoginPage();
            engine.executePopulate(loginPage, loginRaw, context);
            loginPage.performAction("login");


        });

        // -------------------------------------------------
        // STEP 3 – Verify Dashboard Default State
        // -------------------------------------------------

        Steps.run(ctx, "Verify Dashboard", () -> {

            Map<String, Object> verifyDashboard =
                    TestDataLoaderV2.load("DashboardTestData", "TD_VerifySingleProductPrice");

            engine.executeVerify(new DashboardPage(), verifyDashboard, context);
        });

        // -------------------------------------------------
        // STEP 4 – Add Product To Cart
        // -------------------------------------------------
        DashboardPage dashboardPage = new DashboardPage();
        Steps.run(ctx, "Add Product To Cart", () -> {



            dashboardPage.addProductToCart("ZARA COAT 3");


        });


        Steps.run(ctx, "Open the Carts page", () -> {

            dashboardPage.performComponentAction("headerNav", "cart");
        });
        // -------------------------------------------------
        // STEP 5 – Verify Cart Page
        // -------------------------------------------------

        Steps.run(ctx, "Verify Cart Page", () -> {

            Map<String, Object> verifyCart =
                    TestDataLoaderV2.load("MycartsTestData", "TD_VERIFY_CARTPAGE");

            engine.executeVerify(new MycartsPage(), verifyCart, context);
        });
    }
    @Test
    public void registerDefaultStateSmoke() {

        StepContext ctx = new StepContext(
                driver,
                "registerDefaultStateSmoke",
                ExtentTestManager.getTest()
        );

        RuntimeContext context = new RuntimeContext();

        Map<String, Object> raw =
                TestDataLoaderV2.load("RegisterTestData", "TD_DefaultState");

        ExecutionEngineV3 engine = new ExecutionEngineV3();

        Steps.run(ctx, "Verify Register Default State", () -> {
            engine.executeVerify(new RegisterPage(), raw, context);
        });
    }









    @Test
    public void loginV3PopulateTest() {

        StepContext ctx = new StepContext(
                driver,
                "loginV3PopulateTest",
                ExtentTestManager.getTest()
        );

        RuntimeContext context = new RuntimeContext();

         Map<String, Object> raw =
                TestDataLoaderV2.load("LoginTestData", "TD_InvalidLogin");

        ExecutionEngineV3 engine = new ExecutionEngineV3();
        Steps.run(ctx, "Login with Flat TestData", () -> {
            engine.execute(new LoginPage(), raw, context);

        });


        Steps.run(ctx, "Verify entered values", () -> {

            engine.executeVerify(new LoginPage(), raw, context);
        });


    }



    @Test
    public void invalidLoginShouldFail() {

        StepContext ctx = new StepContext(
                driver,
                "Invalid Login Test",
                ExtentTestManager.getTest()
        );

        RuntimeContext context = new RuntimeContext();

        // -------------------------------------------------
        // Load TestData
        // -------------------------------------------------
        Map<String, Object> invalidData =
                TestDataLoaderV2.load(
                        "LoginTestData",
                        "TD_InvalidLogin"
                );

        // -------------------------------------------------
        // Step 1: Attempt invalid login
        // -------------------------------------------------
        Steps.run(ctx, "Attempt login with invalid credentials", () -> {

            LoginPage loginPage = new LoginPage();

            loginPage.populate(invalidData, context);
            loginPage.performAction("login");
        });

        // -------------------------------------------------
        // Step 2: Verify login did not succeed
        // -------------------------------------------------
        Steps.run(ctx, "Verify user remains on login page", () -> {

            // Verify URL still contains login
            Assert.assertTrue(
                    driver.getCurrentUrl().contains("/auth/login"),
                    "Expected to remain on login page"
            );

            // Verify login button still visible
            LoginPage loginPage = new LoginPage();

            loginPage.verifyField(
                    "login",
                    new VerifySpec(ValidationType.IS_VISIBLE)
            );
        });
    }

    @Test
    public void registerAndLoginFlow_V3_Old() {

        /*
        RuntimeContext context = new RuntimeContext();

        // ----------------------------
        // STEP 1 – Register
        // ----------------------------

        Map<String, Object> registerRaw =
                TestDataLoaderV2.load("RegisterPage", "TD_RegisterNewUser");

        ExecutionEngineV3.execute(
                new RegisterPage(),
                registerRaw,
                context
        );

        // ----------------------------
        // STEP 2 – Login
        // ----------------------------

        Map<String, Object> loginRaw =
                TestDataLoaderV3.load("LoginPage", "TD_LoginWithRegisteredUser");

        ExecutionEngineV3.execute(
                new LoginPage(),
                loginRaw,
                context
        );

        // ----------------------------
        // STEP 3 – Verify Dashboard
        // ----------------------------

        Map<String, Object> verifyDashboard =
                TestDataLoaderV2.load("DashboardPage", "TD_VerifySingleProductPrice");

       ExecutionEngineV3.execute(
                new DashboardPage(),
                verifyDashboard,
                context
        );

        // ----------------------------
        // STEP 4 – Add to Cart
        // ----------------------------

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.addProductToCart("ZARA COAT 3");

        dashboardPage.performComponentAction("headerNav", "cart");

        // ----------------------------
        // STEP 5 – Verify Cart Page
        // ----------------------------

        Map<String, Object> verifyCart =
                TestDataLoaderV2.load("MycartsPage", "TD_VERIFY_CARTPAGE");

        ExecutionEngineV3.execute(
                new MycartsPage(),
                verifyCart,
                context
        );


    }


    @Test
    public void registerAndLoginFlowOldDeprecated() {

        StepContext ctx = new StepContext(
                driver,
                "Register and Login Flow",
                ExtentTestManager.getTest()
        );

        RuntimeContext context = new RuntimeContext();

        // -------------------------------------------------
        // Step 1: Load TestData
        // -------------------------------------------------
        Map<String, Object> registerData =
                TestDataLoaderV2.load(
                        "RegisterTestData",
                        "TD_RegisterNewUser"
                );

        Map<String, Object> loginData =
                TestDataLoaderV2.load(
                        "LoginTestData",
                        "TD_LoginWithRegisteredUser"
                );

        Map<String, Object> defaultData =
                TestDataLoaderV2.load(
                        "LoginTestData",
                        "TD_Login_DefaultState"
                );

        // -------------------------------------------------
        // Step 2: Open Login Page & Verify Defaults
        // -------------------------------------------------
        Steps.run(ctx, "Open Login page & Verify default state", () -> {

            LoginPage loginPage = new LoginPage();

            loginPage.verify(defaultData);
        });

        // -------------------------------------------------
        // Step 3: Navigate to Register Page
        // -------------------------------------------------
        Steps.run(ctx, "Click Register link", () -> {

            LoginPage loginPage = new LoginPage();
            loginPage.performAction("registerHere");
        });

        // -------------------------------------------------
        // Step 4: Register New User
        // -------------------------------------------------
        Steps.run(ctx, "Register new user", () -> {

            RegisterPage registerPage = new RegisterPage();

            registerPage.populate(registerData, context);
            registerPage.performAction("login");
        });

        // -------------------------------------------------
        // Step 5: Login with Registered User
        // -------------------------------------------------
        Steps.run(ctx, "Login with generated user", () -> {

            driver.get("https://www.rahulshettyacademy.com/client/#/auth/login");

            LoginPage loginPageAfterRegister = new LoginPage();

            loginPageAfterRegister.populate(loginData, context);
            loginPageAfterRegister.performAction("login");
        });

        DashboardPage dashboardPage = new DashboardPage();
        Steps.run(ctx, "Verify ZARA COAT 3 price", () -> {

            Map<String, Object> verifyData =
                    TestDataLoaderV2.load(
                            "DashboardTestData",
                            "TD_VerifySingleProductPrice"
                    );

            dashboardPage.verify(verifyData);
        });
        dashboardPage.addProductToCart("ZARA COAT 3");
        dashboardPage.performComponentAction("headerNav","cart");
        MycartsPage cartPage = new MycartsPage();
        ComponentSchema cs = cartPage.getComponentSchemas().get("cartItem");
        System.out.println(cs.resolutionStrategy());
        boolean root = cartPage.isProductInCart("ZARA COAT 3");



        System.out.println(root == true ? "Found component root" : "Not found");
    }
/*
    @Test
    public void shouldRegisterAndLogin_usingJsonTestData() {


       // ================= Register =================
        Map<String, Object> registerData =
                TestDataLoaderV2.load("RegistrationPage", "TD_ValidRegister");

        Register register = new Register();

        // Populate only (data-driven)
        register.populate(registerData);

        // Explicit action in code (per contract)
        register.performAction("login");


        // ================= Login =================
        Map<String, Object> loginData =
                TestDataLoaderV2.load("loginPage", "TD_ValidLogin");

        Login login = new Login();

        login.populate(loginData);
        login.performAction("login");
        System.out.println("Wait Here");


    }
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

    @Test
    public void debugTableRowMatching_WT_4_2() {

        driver.get(
                "file:///C:/Users/rchak/OneDrive/Documents/RajibWork/Learning/NewStudyNotes/AgenticAI/demo-form.html"
        );

        // Page initialization (schemas already registered)
        DemoFormPage page = new DemoFormPage();

        System.out.println(
                "DEBUG usersTable present: " +
                        page.table("usersTable").isPresent()
        );

        // Resolve schema via Page (IMPORTANT)
        TableSchema tableSchema = page.getTable("usersTable");

        TableActionExecutor exec =
                new TableActionExecutor(driver, tableSchema);

        // --- WT-4.1 sanity checks ---
        exec.isTablePresent();
        int rowCount = exec.getRowCount();
        System.out.println("Row count = " + rowCount);

        // --- WT-4.2 row matching ---
        Map<String, String> criteria = Map.of(
                "Status", "INACTIVE",
                "Name", "Bob"
        );

        Optional<Integer> matchingRow =
                exec.findFirstMatchingRow(criteria);

        System.out.println("Matching row index: " + matchingRow);

        assertTrue(
                matchingRow.isPresent(),
                "Expected a matching row for criteria: " + criteria
        );

        int rowIndex = matchingRow.get();

        // --- Read-only verification ---
        Map<String, String> rowData =
                exec.getRowAsMap(rowIndex);

        System.out.println("Matched row data: " + rowData);

        assertEquals(rowData.get("Status"), "INACTIVE");
        assertEquals(rowData.get("Name"), "Bob");

        // --- Optional single-cell read ---
        String status =
                exec.getCellValue(rowIndex, "Status");

        assertEquals(status, "INACTIVE");

        System.out.println("WT-4.2 row matching validated successfully");
    }



    @Test
    public void sanity_check_table_verification_loader() {

        File json =
                new File("src/test/resources/testdata/DemoFormPage.json");

        TableVerificationSpec spec =
                TableVerificationLoaderV3.load(
                        json,
                        "Smoke Test",
                        "link_clicked"
                );

        // ---- Sanity assertions ----
        assertEquals(spec.tableKey(), "usersGrid");
        assertEquals(spec.id(), "link_clicked");

        assertFalse(spec.verify().isEmpty());

       RowVerificationSpec row = spec.verify().get(0);

        assertEquals(row.match().get("Name"), "Alice");
        assertEquals(
                row.assertThat().get("Status").value(),
                "ACTIVE"
        );
    }


    @Test
    public void demoForm_tableVerification_containsAssertion() {

        driver.get(
                "file:///C:/Users/rchak/OneDrive/Documents/RajibWork/Learning/NewStudyNotes/AgenticAI/demo-form.html"
        );

        DemoFormPage page = new DemoFormPage();

        File json =
                new File("src/test/resources/testdata/DemoFormPage.json");

        rajib.automation.framework.v3.table.model.TableVerificationSpec spec =
                TableVerificationLoaderV3.load(
                        json,
                        "demoForm_tableVerification_containsAssertion",
                        "status_contains_click"
                );

        TableSchema tableSchema =
                page.getTable(spec.tableKey());

        TableActionExecutor executor =
                new TableActionExecutor(driver, tableSchema);

        TableVerifierV3 verifier =
                new TableVerifierV3(executor, tableSchema);

        verifier.verify(spec);
    }





    @Test
    public void demoForm_FluentTableAction_And_Verification() {

        // 1️⃣ Navigate
        driver.get(
                "file:///C:/Users/rchak/OneDrive/Documents/RajibWork/Learning/NewStudyNotes/AgenticAI/demo-form.html"
        );

        DemoFormPage page = new DemoFormPage();

        // 2️⃣ Load test data (values only)
        File json =
                new File("src/test/resources/testdata/demo-form-testdata.json");

        DemoFormTestData testData =
                DemoFormTestDataLoader.load(json);

        DemoFormTestCase tc =
                testData.getTestCase("Smoke Test");

        // 3️⃣ Populate page fields (existing framework logic)
        page.populate(tc.getData());

        // 4️⃣ Perform TABLE ACTION (imperative, fluent)
        page.usersGrid()
                .row(RowCriteria.where("Name", "Jane Doe"))
                .clickLink("Status");

        // 5️⃣ Read expected value FROM TESTDATA
        String expectedStatus =
                tc.getExpectedTableValue(
                        "usersGrid",
                        "Jane Doe",
                        "Status"
                );

        // 6️⃣ Verify TABLE STATE (imperative assertion)
        page.usersGrid()
                .row(RowCriteria.where("Name", "Jane Doe"))
                .assertCellEquals("Status", expectedStatus);
    }





}

 */
