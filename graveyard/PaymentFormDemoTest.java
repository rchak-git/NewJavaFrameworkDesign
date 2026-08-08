package rajib.automation.framework.tests.demo;



import core.context.StepContext;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.context.RuntimeContextHolder;
import rajib.automation.framework.v3.round2.loaders.TestDataLoaderR2;
import rajib.automation.framework.v3.round2.testdatamodels.TestStepData;
import rajib.automation.framework.v3.round2.utils.StepConverters;
import rajib.automation.framework.v3.round2.engine.CommandDispatcherR2;
import rajib.automation.framework.v3.round2.page.demo.PaymentFormDemo;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import reporting.ExtentTestManager;

import java.util.List;

public class PaymentFormDemoTest extends BaseTest {

    @Test
    public void testPaymentForm_validPopulationAndSubmit_v1() throws InterruptedException {
        RuntimeContext context = RuntimeContextHolder.get();

        driver.get("http://localhost:8080/payment-form");

        List<TestStepData> steps = TestDataLoaderR2.loadResolvedTestSteps(
                "testdata/DemoTestData/PaymentFormOnlyTestData.json",
                "Test1_CreatePaymentFormOnly"
        );

        StepContext stepContext = new StepContext(
                driver,
                "testPaymentForm_validPopulationAndSubmit_v1",
                ExtentTestManager.getTest()
        );

        List<ControlCommand> commands = steps.stream()
                .map(StepConverters::fromTestStepData)
                .toList();

        PaymentFormDemo formPage = new PaymentFormDemo(new ElementResolver(driver), context);
        CommandDispatcherR2.execute(formPage, commands, stepContext);

        System.out.println("Wait Here");
    }
}
