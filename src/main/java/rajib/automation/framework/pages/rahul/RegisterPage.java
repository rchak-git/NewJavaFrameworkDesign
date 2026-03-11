package rajib.automation.framework.pages.rahul;

import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.enums.FieldType;
import java.util.*;

public class RegisterPage extends BasePage {

    public RegisterPage() {

        initSchemas();

        pageFields.put("firstName",
                new PageField(
                        "firstName",
                        "id",
                        "firstName",
                        FieldType.TEXTBOX,
                        "firstName"
                )
        );

        pageFields.put("lastName",
                new PageField(
                        "lastName",
                        "id",
                        "lastName",
                        FieldType.TEXTBOX,
                        "lastName"
                )
        );

        pageFields.put("userEmail",
                new PageField(
                        "userEmail",
                        "id",
                        "userEmail",
                        FieldType.TEXTBOX,
                        "userEmail"
                )
        );

        pageFields.put("userMobile",
                new PageField(
                        "userMobile",
                        "id",
                        "userMobile",
                        FieldType.TEXTBOX,
                        "userMobile"
                )
        );

        pageFields.put("occupation",
                new PageField(
                        "occupation",
                        "css",
                        "select[formcontrolname='occupation']",
                        FieldType.DROPDOWN,
                        "occupation"
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

        pageFields.put("confirmPassword",
                new PageField(
                        "confirmPassword",
                        "id",
                        "confirmPassword",
                        FieldType.TEXTBOX,
                        "confirmPassword"
                )
        );

        pageFields.put("required",
                new PageField(
                        "required",
                        "css",
                        "input[type='checkbox'][formcontrolname='required']",
                        FieldType.CHECKBOX,
                        "required"
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

        pageFields.put("male",
                new PageField(
                        "male",
                        "css",
                        "input[type='radio'][value='Male']",
                        FieldType.RADIO,
                        "male"
                )
        );

        pageFields.put("female",
                new PageField(
                        "female",
                        "css",
                        "input[type='radio'][value='Female']",
                        FieldType.RADIO,
                        "female"
                )
        );

        compositePageFields.put("gender", new ArrayList<>());
        compositePageFields.get("gender").add(pageFields.get("male"));
        compositePageFields.get("gender").add(pageFields.get("female"));

    }
}
