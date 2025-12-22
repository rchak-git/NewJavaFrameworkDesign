package rajib.automation.framework.tests.Miscelleneous;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.pages.home.HomePage;
import rajib.automation.framework.pages.login.LoginPage;

public class EnterKey extends BaseTest {

   public WebElement elemSearch ;
    @BeforeMethod
    public void setupTest()
    {
        driver.get("https://www.google.com/");
        this.elemSearch = driver.findElement(By.xpath("//textArea[@title='Search']"));

    };
    @Test
    public void searchUsingDirectEnter() {

        elemSearch.sendKeys("Selenium WebDriver");
        elemSearch.sendKeys(Keys.ENTER);
        System.out.println("Wait Here");
    }

    @Test
    public void searchUsingGlobalEnter() {
        elemSearch.sendKeys("Selenium WebDriver");
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).perform();
        System.out.println("Wait Here");
    }

    @Test
    public void searchUsingActionsEnter() {
        elemSearch.sendKeys("Selenium WebDriver");
        Actions actions = new Actions(driver);
        actions.sendKeys(elemSearch,Keys.ENTER).perform();
        System.out.println("Wait Here");
    }


}