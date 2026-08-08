Variables:
- TEST_DATA_FOLDER = testdata/payments/
- TEST_DATA_FILE_NAME = PaymentFormOnlyTestData.json
- TEST_NAME_IN_JSON = Test1_CreatePaymentFormOnly
- TARGET_PAGE_CLASS = rajib.automation.framework.v3.round2.page.demo.PaymentFormDemo
- TEST_METHOD_NAME = testPaymentForm_validPopulationAndSubmit_v1
- VALIDATION_TYPE = ValidationType.TEXT_EQUALS

Reference test method:
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

Reference test data:
[
{
"scenarioId": "ValidPaymentFormPopulation",
"steps": [
{ "fieldKey": "customerName", "intent": "populate", "value": "${customerName}" },
{ "fieldKey": "amount", "intent": "populate", "value": "${amount}" },
{ "fieldKey": "scenario", "intent": "populate", "value": "${scenario}" }
]
},
{
"testName": "Test1_CreatePaymentSmoke",
"scenario": [
{
"use": "ValidPaymentFormPopulation",
"parameters": {
"customerName": "Ravi",
"amount": "100.00",
"scenario": "SUCCESS"
}
},
{ "fieldKey": "submit", "intent": "action", "actionType": "CLICK" }
]
}
]

Task:
Generate a new test for the same framework style, but only for the PaymentFormDemo flow up to the Submit button click.

Hard requirements:
- Use exactly TARGET_PAGE_CLASS in the generated Java file
- Do not use PaymentFormPage_v1
- Import the page object from the demo package
- Output the complete Java file, including package, imports, class, and test method
- Output the complete JSON file
- Keep Java and JSON in separate fenced blocks
- Do not include any explanation
- Do not include any extra text before, between, or after the blocks

Business intent:
- Populate the payment form with valid data
- Verify the customerName field using VALIDATION_TYPE before clicking Submit
- Click the Submit button

Test flow in English:
1. Enter a customer name into the customerName field.
2. Enter an amount into the amount field.
3. Select a scenario value in the scenario dropdown.
4. Verify that the customerName field exactly equals the entered value using VALIDATION_TYPE.
5. Click the Submit button.

Exact test data file details:
- Folder path: TEST_DATA_FOLDER
- File name: TEST_DATA_FILE_NAME
- The test method must load this exact file using TestDataLoaderR2.loadResolvedTestSteps(...)
- The test method must use the test name: TEST_NAME_IN_JSON

Framework rules:
- Follow the same execution pattern as the reference test method
- Use TestDataLoaderR2.loadResolvedTestSteps(...)
- Convert TestStepData to ControlCommand using StepConverters.fromTestStepData
- Execute commands using CommandDispatcherR2
- Use the exact target page class specified above
- Preserve the existing JSON structure and scenario-driven model
- Keep reusable population scenarios separate from verification scenarios
- Do not invent a new DSL or JSON format
- Do not generate PaymentHistoryPage code or test data
- Do not add post-submit verification on another page
- Do not add button verification if the button control does not support verify
- Use supported verification only for controls that already implement verify
- Keep the output aligned with the same naming style as the reference tests

Output format requirements:
1. First provide only the complete Java file in a fenced java code block
2. Then provide only the complete JSON file in a separate fenced json code block
3. Do not mix the method and JSON together in one block
4. Do not add explanation between the two code blocks
5. Do not place any code outside the two code blocks

Expected output:
- One complete Java file
- One complete JSON file
- Both aligned with the reference flow and framework behavior