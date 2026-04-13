package rajib.automation.framework.v3.round2.resolver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.utils.WaitUtils;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;

public class ElementResolver {

    private final WebDriver driver;

    public ElementResolver() {
        this.driver = DriverFactory.getDriver();
    }

    public ElementResolver(WebDriver driver) {
        this.driver = driver;
    }

    // For single-locator fields
    public WebElement resolve(FieldSchema schema) {
        // Typical contract is "main" locator
        LocatorSchema locator = schema.locators.get("main"); // or use a param if choice varies

        if (locator == null) {
            throw new IllegalStateException(
                    "Locator is missing for field: " + schema.key
            );
        }
        By by = toBy(locator);
        return WaitUtils.waitForVisible(by);
    }

    // For multi-locator fields (e.g., DualListBoxControl)
    public WebElement resolve(FieldSchema schema, String locatorKey) {
        LocatorSchema locator = schema.locators.get(locatorKey);
        if (locator == null) {
            throw new IllegalStateException(
                    "Locator '" + locatorKey + "' is missing for field: " + schema.key
            );
        }
        By by = toBy(locator);
        return WaitUtils.waitForVisible(by);
    }

    private By toBy(LocatorSchema locator) {
        String strategy = locator.strategy;
        String value = locator.value;

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