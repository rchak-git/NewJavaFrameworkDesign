package rajib.automation.framework.tests.smoke;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;

public class SmokTest extends BaseTest {


    @Test
    public void navigateDemoWebSite()
    {
     wait.until(ExpectedConditions.titleIs("STORE"));

    }


}
