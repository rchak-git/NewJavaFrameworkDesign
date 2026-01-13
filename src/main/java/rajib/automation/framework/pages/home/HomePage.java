
package rajib.automation.framework.pages.home;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


// Create HomePage class with constructor, locator example and a sample method
public class HomePage {
    private WebDriver driver;

    // Locator example
    private By welcomeMessage = By.id("welcome-message");

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Sample method to get welcome message text
    public String getWelcomeMessage() {
        return driver.findElement(welcomeMessage).getText();
    }
}



