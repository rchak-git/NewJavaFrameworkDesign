package rajib.automation.framework.tests.Miscelleneous;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;

public class DomPropertyTest extends BaseTest {
    @Test
    public void javaExecutor() {
       driver.get("https://practicetestautomation.com/practice-test-login/");
     // WebElement elemUserName =  driver.findElement(By.name("username"));
       // System.out.println("The DOM attribute 'localName has value: " + elemUserName.getDomProperty("localName"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement userName = (WebElement) js.executeScript(" return document.querySelectorAll('input').find(e-> e.localName =='input')[0]");
        userName.sendKeys("student");
       driver.findElement(By.name("password")).sendKeys("Password123");
       driver.findElement(By.id("submit")).click();
        System.out.println("Wait Here");
    }


}