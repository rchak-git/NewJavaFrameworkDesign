package rajib.automation.framework.tests.Miscelleneous;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.pages.home.HomePage;
import rajib.automation.framework.pages.login.LoginPage;
import rajib.automation.framework.utils.PageFactory;

public class LoginTest extends BaseTest {
    @Test
    public void verifyLogin() throws InterruptedException {
       driver.get("https://www.practicetestautomation.com/practice-test-login/");
       Thread.sleep(5000);
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.F5).build().perform();
       driver.findElement(By.name("username")).sendKeys("student");
       driver.findElement(By.name("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();
        System.out.println("Wait Here");
    }

  /*  @Test
    public void testLoginFlow() throws InterruptedException {

        HomePage home = new HomePage();
        home.clickLoginLink();

        LoginPage login = new LoginPage();
        login.enterUsername("testuser");
        login.enterPassword("testpass");
        login.clickLoginButton();

        Thread.sleep(3000); // temporary pause to visually confirm flow (remove later)
    }


    @Test
    public void testLoginFlowWithPageFactory() throws InterruptedException {

        HomePage home = new HomePage();
        home.clickLoginLink();

        LoginPage login = PageFactory.getPage(LoginPage.class);
        login.enterUsername("testuser");
        login.enterPassword("testpass");
        login.clickLoginButton();

        Thread.sleep(3000); // temporary pause to visually confirm flow (remove later)
    }

   */
}