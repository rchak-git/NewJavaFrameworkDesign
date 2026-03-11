package rajib.automation.framework.resolution;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import rajib.automation.framework.v2.resolver.ElementResolver;


public class ComponentResolver implements ElementResolver {

    private final WebElement root;

    public ComponentResolver(WebElement root) {
        this.root = root;
    }

    @Override
    public WebElement resolve(By locator) {
        return root.findElement(locator);
    }
}
