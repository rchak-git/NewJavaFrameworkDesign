package rajib.automation.framework.tests.Miscelleneous;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.factory.DriverFactory;

import java.util.Set;

public class WindowHandles extends BaseTest {

    @Test
    public void openMultipleWindows()
    {
        WebDriver  driver = DriverFactory.getDriver();
        driver.get("http://omayo.blogspot.com/");
        String currWindowhandle = driver.getWindowHandle();
        driver.findElement(By.linkText("SeleniumTutorial")).click();
      Set<String> setHandles =  driver.getWindowHandles();
        String switchHandle ="";
        for (String handle : setHandles)
        {
            if ( !handle.equalsIgnoreCase(currWindowhandle))
            {
                switchHandle = handle;
                break;
            }

        }
      driver.switchTo().window(switchHandle);
        String currUrl = driver.getCurrentUrl();
        Assert.assertEquals("https://selenium143.blogspot.com/",currUrl);

     driver.close();
     driver.switchTo().window(currWindowhandle);
    }
}
