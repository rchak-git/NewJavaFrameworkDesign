package agent.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginStateObserver {

    public static void observe(WebDriver driver, LoginAgentState state) {

        state.usernamePresent = isPresent(driver, By.id("user-name"));
        state.passwordPresent = isPresent(driver, By.id("password"));
        state.loginButtonEnabled = isEnabled(driver, By.id("login-button"));

        state.errorVisible = isPresent(driver, By.cssSelector("[data-test='error']"));
        state.errorText = state.errorVisible
                ? driver.findElement(By.cssSelector("[data-test='error']")).getText()
                : null;

        state.goalReached =
                driver.getCurrentUrl().contains("inventory");

        state.captchaDetected = false;
    }

    private static boolean isPresent(WebDriver driver, By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isEnabled(WebDriver driver, By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
}
