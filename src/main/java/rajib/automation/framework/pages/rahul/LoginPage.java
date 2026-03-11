package rajib.automation.framework.pages.rahul;

import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.enums.FieldType;
import java.util.*;

public class LoginPage extends BasePage {

    public LoginPage() {

        initSchemas();

        pageFields.put("userEmail",
                new PageField(
                        "userEmail",
                        "id",
                        "userEmail",
                        FieldType.TEXTBOX,
                        "userEmail"
                )
        );

        pageFields.put("userPassword",
                new PageField(
                        "userPassword",
                        "id",
                        "userPassword",
                        FieldType.TEXTBOX,
                        "userPassword"
                )
        );

        pageFields.put("login",
                new PageField(
                        "login",
                        "id",
                        "login",
                        FieldType.ACTION,
                        "login"
                )
        );

        pageFields.put("forgotPassword",
                new PageField(
                        "forgotPassword",
                        "css",
                        "a.forgot-password-link",
                        FieldType.ACTION,
                        "forgotPassword"
                )
        );

        pageFields.put("registerHere",
                new PageField(
                        "registerHere",
                        "css",
                        "p.login-wrapper-footer-text a.text-reset",
                        FieldType.ACTION,
                        "registerHere"
                )
        );

    }
}
