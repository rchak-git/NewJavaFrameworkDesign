package rajib.automation.framework.tests.payments;

import static org.testng.Assert.assertTrue;

import java.sql.DriverManager;
import java.util.List;

import core.context.StepContext;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.context.RuntimeContextHolder;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.engine.CommandDispatcherR2;
import rajib.automation.framework.v3.round2.loaders.TestDataLoaderR2;
import rajib.automation.framework.v3.round2.page.PaymentFormPage_v1;
import rajib.automation.framework.v3.round2.page.payment.PaymentHistoryPage;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;
import rajib.automation.framework.v3.round2.testdatamodels.TestStepData;
import rajib.automation.framework.v3.round2.utils.StepConverters;
import reporting.ExtentTestManager;


public class PaymentFormSmokeTest extends BaseTest {

    @Test
    public void testPaymentForm_validPopulationAndSubmit_v1() {
        RuntimeContext context = RuntimeContextHolder.get();

        driver.get("http://localhost:8080/payment-form");

        List<TestStepData> steps = TestDataLoaderR2.loadResolvedTestSteps(
                "testdata/payments/PaymentFormTestData.json",
                "Test1_CreatePaymentSmoke"
        );

        StepContext stepContext = new StepContext(
                driver,
                "testPaymentForm_validPopulationAndSubmit_v1",
                ExtentTestManager.getTest()
        );

        List<ControlCommand> commands = steps.stream()
                .map(StepConverters::fromTestStepData)
                .toList();

        PaymentFormPage_v1 formPage = new PaymentFormPage_v1(new ElementResolver(driver), context);

        CommandDispatcherR2.execute(formPage, commands, stepContext);

        assertTrue(
                driver.getCurrentUrl().contains("/payments"),
                "User was not redirected to /payments page."
        );

        System.out.println("Wait Here");
    }


    @Test
    public void testPaymentForm_validPopulationAndSubmit_v2() throws InterruptedException {
        RuntimeContext context = RuntimeContextHolder.get();

        driver.get("http://localhost:8080/payment-form");

        List<TestStepData> steps = TestDataLoaderR2.loadResolvedTestSteps(
                "testdata/payments/PaymentFormTestData.json",
                "Test1_CreatePaymentSmoke"
        );

        StepContext stepContext = new StepContext(
                driver,
                "testPaymentForm_validPopulationAndSubmit_v2",
                ExtentTestManager.getTest()
        );

        List<ControlCommand> commands = steps.stream()
                .map(StepConverters::fromTestStepData)
                .toList();

        PaymentFormPage_v1 formPage = new PaymentFormPage_v1(new ElementResolver(driver), context);
        CommandDispatcherR2.execute(formPage, commands, stepContext);

        assertTrue(
                driver.getCurrentUrl().contains("/payments"),
                "User was not redirected to /payments page."
        );

        List<TestStepData> historySteps = TestDataLoaderR2.loadResolvedTestSteps(
                "testdata/payments/PaymentHistoryTestData.json",
                "Test1_VerifyCreatedPayment"
        );

        List<ControlCommand> historyCommands = historySteps.stream()
                .map(StepConverters::fromTestStepData)
                .toList();

        PaymentHistoryPage historyPage = new PaymentHistoryPage(new ElementResolver(driver), context);
        CommandDispatcherR2.execute(historyPage, historyCommands, stepContext);

        System.out.println("Wait Here");
    }
}