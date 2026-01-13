import org.openqa.selenium.By;
import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.model.PageField;

public class LoginPage extends BasePage {

    public LoginPage() {

        pageFields.put("username",
            new PageField(
                "username",
                By.id("user-name"),
                FieldType.TEXTBOX,
                "username"
            )
        );

        pageFields.put("password",
            new PageField(
                "password",
                By.id("password"),
                FieldType.TEXTBOX,
                "password"
            )
        );

        pageFields.put("loginButton",
            new PageField(
                "loginButton",
                By.id("login-button"),
                FieldType.ACTION,
                null
            )
        );

        pageFields.put("loginError",
            new PageField(
                "loginError",
                By.cssSelector("[data-test='error']"),
                FieldType.LABEL,
                null
            )
        );

    }
}
