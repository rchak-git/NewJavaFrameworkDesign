package rajib.automation.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import rajib.automation.framework.factory.DriverFactory;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class WaitUtils {

    private static final int DEFAULT_TIMEOUT =
            ConfigReader.getIntProperty("explicitWaitTimeInSecs");

    // Prevent instantiation
    private WaitUtils() {}

    public static <T> T waitUntilReturn(
            Supplier<T> supplier,
            Predicate<T> condition
    ) {

        WebDriverWait wait = new WebDriverWait(
                DriverFactory.getDriver(),
                Duration.ofSeconds(10)
        );

        return wait.until(driver -> {
            T value = supplier.get();

            if (value != null && condition.test(value)) {
                return value;
            }

            return null;
        });
    }


    /* -------------------------
       Internal driver resolver
       ------------------------- */
    private static WebDriver driver() {
        return DriverFactory.getDriver();
    }

    /* -------------------------
       Wait methods
       ------------------------- */

      private static final long POLLING_INTERVAL_MS = 200;

    public static void waitUntil(Supplier<Boolean> condition,
                                 String failureMessage) {

        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < DEFAULT_TIMEOUT) {
            try {
                if (condition.get()) {
                    return;
                }
            } catch (Exception ignored) {
                // Ignore transient exceptions (stale element, etc.)
            }

            try {
                Thread.sleep(POLLING_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", e);
            }
        }

        throw new TimeoutException(failureMessage);
    }


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

    public static WebElement waitForVisible(By locator) {

        return new WebDriverWait(
                driver(),
                Duration.ofSeconds(DEFAULT_TIMEOUT)
        ).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    public static WebElement waitForVisible(By locator, int timeoutInSecs) {

        return new WebDriverWait(
                driver(),
                Duration.ofSeconds(timeoutInSecs)
        ).until(ExpectedConditions.visibilityOfElementLocated(locator));
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
