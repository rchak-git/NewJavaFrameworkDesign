package rajib.automation.framework.pages.login;

import rajib.automation.framework.utils.MyFindBy;

public class LoginPageFactory {

    @MyFindBy(id = "username")
    private String userField;

    @MyFindBy(id = "password")
    private String passwordField;

    private String nonAnnotatedField; // should NOT be picked up
}
