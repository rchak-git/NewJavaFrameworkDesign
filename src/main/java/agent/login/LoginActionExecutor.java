package agent.login;

import agent.AgentAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginActionExecutor {

    public void execute(AgentAction action, LoginContext context) {

        WebDriver driver = context.getDriver();

        switch (action) {

            case ENTER_USERNAME:
                WebElement username =
                        driver.findElement(By.id("user-name"));
                username.clear();                 // 🔴 important
                username.sendKeys(context.getUsername());
                break;

            case ENTER_PASSWORD:
                WebElement password =
                        driver.findElement(By.id("password"));
                password.clear();                 // 🔴 important
                password.sendKeys(context.getPassword());
                break;

            case CLICK_LOGIN:
                driver.findElement(By.id("login-button")).click();
                break;

            case WAIT:
                sleep(1000);
                break;

            case ESCALATE:
                throw new IllegalStateException(
                        "Agent escalation triggered – human intervention required"
                );

            case STOP:
                // No-op: handled by agent loop
                break;

            default:
                throw new IllegalStateException(
                        "Unsupported AgentAction: " + action
                );
        }



    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
