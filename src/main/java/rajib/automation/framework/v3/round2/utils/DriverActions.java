package rajib.automation.framework.v3.round2.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import rajib.automation.framework.factory.DriverFactory;
import rajib.automation.framework.utils.RetryUtils;

public class DriverActions {

    private static final int DEFAULT_TIMEOUT = 10; // seconds

    // Prevent instantiation
    private DriverActions() {}

    /**
     * Helper method to scroll element into view and wait for clickability.
     */
    public static void prepareForInteraction(WebElement element) {
        WebDriver driver = DriverFactory.getDriver();
        // Scroll to the element (center for best cross-browser effect)
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element
        );
        // Wait for clickability
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Safe click attempt: scrolls, waits, retries, JS fallback.
     */
    public static void click(WebElement element) {
        WebDriver driver = DriverFactory.getDriver();

        try {
            prepareForInteraction(element);

            RetryUtils.retryOnException(
                    () -> {
                        element.click();
                        System.out.println("Normal click succeeded for element: " + element);
                    },
                    ElementClickInterceptedException.class,
                    WebDriverException.class
            );
        } catch (WebDriverException ex) {
            // Fallback: JavaScript click
            System.out.println("FALLBACK: JavaScript click for element due to intercepted click");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Safe double click: scrolls, waits, retries.
     */
    public static void doubleClick(WebElement element) {
        WebDriver driver = DriverFactory.getDriver();

        prepareForInteraction(element);

        RetryUtils.retryOnException(
                () -> new Actions(driver).doubleClick(element).perform(),
                WebDriverException.class
        );
    }

    /**
     * Safe typing method—scrolls, waits, clears, then types.
     */
    public static void type(WebElement element, String text) {
        WebDriver driver = DriverFactory.getDriver();

        prepareForInteraction(element); // Ensures visibility and scroll, just like for click()

        RetryUtils.retryOnException(
                () -> { element.clear(); element.sendKeys(text); },
                StaleElementReferenceException.class, WebDriverException.class
        );
    }

    // Add more as needed: rightClick, hover, selectDropdown, etc.
}