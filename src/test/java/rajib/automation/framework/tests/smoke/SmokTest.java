package rajib.automation.framework.tests.smoke;


import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;

public class SmokTest extends BaseTest {


    @Test
    @Parameters({"username"})
    public void navigateDemoWebSite(String username)
    {
      //  System.out.println(url);
        System.out.println(username);
    // wait.until(ExpectedConditions.titleIs("STORE"));

    }


}
