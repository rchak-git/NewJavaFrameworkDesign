package rajib.automation.framework.v2.resolver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface ElementResolver {
    WebElement resolve(By locator);
}