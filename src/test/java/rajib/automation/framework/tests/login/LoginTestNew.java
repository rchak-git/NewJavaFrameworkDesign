package rajib.automation.framework.tests.login;

import com.aventstack.extentreports.ExtentReports;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.model.TestData;
import rajib.automation.framework.pages.home.HomePage;
import rajib.automation.framework.pages.login.LoginPage;
import rajib.automation.framework.utils.TestDataLoader;
import reporting.ExtentManager;
import reporting.ExtentTestManager;


import java.util.Map;
@Listeners(reporting.ExtentTestListener.class)
public class LoginTestNew extends BaseTest {
    //Write a Testng test to use  LoginPage and assert welcome message from HomePage
    @Test
    public void shouldLoginWithValidCredentials() {
        // ---------- Test Steps ----------

        LoginPage loginPage = new LoginPage();
        TestData td = TestDataLoader.load("loginPage", "validUser");
        loginPage.setTestData(td);

        loginPage.populateFields();
        loginPage.performAction("loginButton");
        // ---------- Validation ----------
        Assert.assertTrue(
                driver.getCurrentUrl().contains("inventory"),
                "Login failed: User did not land on Products page"
        );

    }


    /*
    @Test
    public void invalidLoginShouldShowErrorMessage() {

        TestData testData = new TestData(Map.of(
                "username", "standard_user",
                "password", "secret_sauceXYZ"
        ));



        // Step 3: Load page
        LoginPage loginPage = new LoginPage();

        // Step 4: Populate fields (username + password)
        loginPage.populateFields(testData);

        // Step 5: Perform login action
        loginPage.performAction("loginButton");

        // Step 6: Verify expected UI state
        loginPage.verifyField("loginError",testData);
    }

     */






}
