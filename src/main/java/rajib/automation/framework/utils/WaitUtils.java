package rajib.automation.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import rajib.automation.framework.factory.DriverFactory;

import java.time.Duration;

public final class WaitUtils {

    private static final int DEFAULT_TIMEOUT =
            ConfigReader.getIntProperty("explicitWaitTimeInSecs");

    // Prevent instantiation
    private WaitUtils() {}

    /* -------------------------
       Internal driver resolver
       ------------------------- */
    private static WebDriver driver() {
        return DriverFactory.getDriver();
    }

    /* -------------------------
       Wait methods
       ------------------------- */

    public static void waitForTitle(String title) {
        try {
            new WebDriverWait(driver(), Duration.ofSeconds(DEFAULT_TIMEOUT))
                    .until(ExpectedConditions.titleIs(title));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Expected title: [" + title + "] but did not match within timeout", e
            );
        }
    }

    public static void waitForVisible(By locator) {
        new WebDriverWait(driver(), Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForVisible(By locator, int timeoutInSecs) {
        new WebDriverWait(driver(), Duration.ofSeconds(timeoutInSecs))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForClickable(By locator) {
        new WebDriverWait(driver(), Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitForClickable(By locator, int timeoutInSecs) {
        new WebDriverWait(driver(), Duration.ofSeconds(timeoutInSecs))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }
}
