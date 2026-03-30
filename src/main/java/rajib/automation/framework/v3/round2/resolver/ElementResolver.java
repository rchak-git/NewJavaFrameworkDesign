package rajib.automation.framework.v3.round2.resolver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.utils.WaitUtils;

public class ElementResolver {

    private final WebDriver driver;


    public ElementResolver() {

        this.driver = DriverFactory.getDriver();
    }

    public ElementResolver(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement resolve(FieldSchema schema) {

        LocatorSchema locator = schema.locator();

        if (locator == null) {
            throw new IllegalStateException(
                    "Locator is missing for field: " + schema.key()
            );
        }

        By by = toBy(locator);

        // ✅ Use centralized wait utility
        return WaitUtils.waitForVisible(by);
    }
    private By toBy(LocatorSchema locator) {

        String strategy = locator.strategy();
        String value = locator.value();

        return switch (strategy.toLowerCase()) {

            case "id" -> By.id(value);
            case "name" -> By.name(value);
            case "css" -> By.cssSelector(value);
            case "xpath" -> By.xpath(value);
            case "class" -> By.className(value);
            case "tag" -> By.tagName(value);

            default -> throw new IllegalArgumentException(
                    "Unsupported locator strategy: " + strategy
            );
        };
    }
}