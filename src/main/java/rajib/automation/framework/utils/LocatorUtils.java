package rajib.automation.framework.utils;



import org.openqa.selenium.By;
import rajib.automation.framework.codegen.schema.LocatorSchema;

public final class LocatorUtils {

    private LocatorUtils() {
        // utility class
    }

    public static By buildBy(LocatorSchema locator) {

        return switch (locator.strategy().toLowerCase()) {

            case "id" -> By.id(locator.value());
            case "css" -> By.cssSelector(locator.value());
            case "xpath" -> By.xpath(locator.value());

            default -> throw new IllegalArgumentException(
                    "Unsupported locator strategy: " + locator.strategy()
            );
        };
    }

    public static By buildBy(String strategy, String value) {

        return switch (strategy.toLowerCase()) {

            case "id" -> By.id(value);
            case "css" -> By.cssSelector(value);
            case "xpath" -> By.xpath(value);

            default -> throw new IllegalArgumentException(
                    "Unsupported locator strategy: " + strategy
            );
        };
    }
}
