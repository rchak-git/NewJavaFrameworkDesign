package rajib.automation.framework.tests.registration;

import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;

public class RegistrationTests extends BaseTest {

    @Test(groups = {"smoke"})
    public void TC_REG_001_navigate_to_registration_page() {

        executeIntent("NAVIGATE_TO_REGISTRATION");
    }

    @Test(groups = {"regression"})
    public void TC_REG_003_register_with_mandatory_fields() {

        executeIntent("REGISTER_USER", "MANDATORY_ONLY");

        System.out.println("Wait here");
    }

    @Test(groups = {"regression"})
    public void TC_REG_005_register_with_all_fields() {
        executeIntent("REGISTER_USER", "ALL_FIELDS");
    }
}
