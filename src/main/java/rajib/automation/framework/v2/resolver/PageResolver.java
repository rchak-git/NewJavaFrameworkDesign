package rajib.automation.framework.v2.resolver;



import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.utils.WaitUtils;


public class PageResolver implements ElementResolver {

    @Override
    public WebElement resolve(By locator) {
        WaitUtils.waitForVisible(locator);
        return DriverFactory.getDriver().findElement(locator);
    }
}
