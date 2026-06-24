package rajib.automation.framework.v3.round2.resolver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.utils.WaitUtils;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.TableSchema;

public class ElementResolver {

    private final WebDriver driver;

    public ElementResolver() {
        this.driver = DriverFactory.getDriver();
    }

    public ElementResolver(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    // For single-locator fields
    public WebElement resolve(FieldSchema schema) {
        LocatorSchema locator = schema.getLocators().get("main");
        if (locator == null) {
            throw new IllegalStateException(
                    "Locator is missing for field: " + schema.getKey()
            );
        }
        By by = toBy(locator);
        return WaitUtils.waitForVisible(by);
    }

    // For multi-locator fields (e.g., DualListBoxControl)
    public WebElement resolve(FieldSchema schema, String locatorKey) {
        LocatorSchema locator = schema.getLocators().get(locatorKey);
        if (locator == null) {
            throw new IllegalStateException(
                    "Locator '" + locatorKey + "' is missing for field: " + schema.getKey()
            );
        }
        By by = toBy(locator);
        return WaitUtils.waitForVisible(by);
    }

    // For tables: resolve table root using the table locator
    public WebElement resolve(TableSchema schema) {
        LocatorSchema locator = schema.getTableLocator();
        if (locator == null) {
            throw new IllegalStateException(
                    "Table locator is missing for table: " + schema.getKey()
            );
        }
        By by = toBy(locator);
        return WaitUtils.waitForVisible(by);
    }

    // For tables: resolve any table-related locator (table/row/cell) by key
    public WebElement resolve(TableSchema schema, String locatorKey) {
        LocatorSchema locator = switch (locatorKey) {
            case "table" -> schema.getTableLocator();
            case "row" -> schema.getRowLocator();
            case "cell" -> schema.getCellLocator();
            default -> null;
        };

        if (locator == null) {
            throw new IllegalStateException(
                    "Table locator '" + locatorKey + "' is missing for table: " + schema.getKey()
            );
        }

        By by = toBy(locator);
        return WaitUtils.waitForVisible(by);
    }

    private By toBy(LocatorSchema locator) {
        String strategy = locator.getStrategy();
        String value = locator.getValue();

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