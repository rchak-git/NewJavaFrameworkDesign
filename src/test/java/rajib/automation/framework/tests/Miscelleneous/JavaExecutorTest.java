package rajib.automation.framework.tests.Miscelleneous;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;

public class JavaExecutorTest extends BaseTest {
    @Test
    public void javaExecutor() {
       driver.get("https://practicetestautomation.com/practice-test-login/");
      WebElement elemUserName =  driver.findElement(By.name("username"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
       js.executeScript("arguments[0].value =arguments[1]",elemUserName,"student");
        elemUserName.clear();
        elemUserName.sendKeys("Hello");
       driver.findElement(By.name("password")).sendKeys("Password123");
       driver.findElement(By.id("submit")).click();
        System.out.println("Wait Here");
    }


}