package rajib.automation.framework.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class PracticeTests {

    @Test
    public void shouldStartChrome_usingSeleniumManager() {
        WebDriver driver = new ChromeDriver(); // Selenium Manager resolves driver automatically
        driver.get("https://the-internet.herokuapp.com/checkboxes");
        driver.quit();
    }
}