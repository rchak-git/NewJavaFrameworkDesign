package rajib.automation.framework.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;

public class SmokeReact extends BaseTest {

    @Test
    public void smokeReactTest()
    {
        driver.get("http://localhost:3000");
        WebElement inputBox = driver.findElement(By.id("testbox"));
        inputBox.sendKeys("Hello Selenium!");
        System.out.println("Wait here");

    }
}
