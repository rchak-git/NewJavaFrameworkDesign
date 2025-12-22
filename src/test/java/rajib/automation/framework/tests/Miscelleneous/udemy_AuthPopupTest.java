package rajib.automation.framework.tests.Miscelleneous;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.pages.home.HomePage;
import rajib.automation.framework.pages.login.LoginPage;

public class udemy_AuthPopupTest extends BaseTest {
    @Test
    public void popupBasicAuth() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");
        System.out.println("Wait Here");
    }


}