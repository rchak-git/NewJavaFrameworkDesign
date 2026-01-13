package agent.login;

import core.context.StepContext;
import org.openqa.selenium.WebDriver;

public class LoginContext {

    private final WebDriver driver;
    private final StepContext stepContext;
    private final String username;
    private final String password;

    public LoginContext(
            WebDriver driver,
            StepContext stepContext,
            String username,
            String password
    ) {
        this.driver = driver;
        this.stepContext = stepContext;
        this.username = username;
        this.password = password;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public StepContext getStepContext() {

        return stepContext;
    }

    public String getUsername() {
        return  username;
    }

    public String getPassword() {
        return password;
    }
}
