package rajib.automation.framework.tests.Miscelleneous;

import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;

public class udemy_AuthPopupTest extends BaseTest {
    @Test
    public void popupBasicAuth() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");
        System.out.println("Wait Here");
    }


}