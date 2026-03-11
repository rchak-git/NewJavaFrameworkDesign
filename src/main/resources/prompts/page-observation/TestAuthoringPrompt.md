You are generating a JUnit test method for a custom Java Selenium automation framework.

IMPORTANT FRAMEWORK RULES (MANDATORY):

1. You MUST use the existing Page class that is currently open in the workspace.
2. You MUST NOT use driver.findElement directly.
3. You MUST NOT create new PageField or TableActionExecutor manually.
4. You MUST NOT bypass the framework abstraction.
5. All populate, verify, and action logic MUST go through ExecutionEngineV3.
6. Use RuntimeContext for execution.
7. Load TestData from JSON file located in:
   src/test/resources/testdata/

TEST METHOD RULES:

- Use JUnit 5 style.
- Instantiate:
    - DemoFormPage
    - ExecutionEngineV3
    - RuntimeContext
- Load the test data JSON as Map<String, Object>
- Extract the scenario by key.
- Call:
  executionEngine.execute(page, scenarioData, context);
  OR
  executionEngine.executeVerify(page, scenarioData, context);
  based on test steps.

- Do NOT manually perform table operations.
- Do NOT manually perform assertions.
- Do NOT use TableActionExecutor directly.

The test method should be clean, minimal, and framework-aligned.

Navigate to the Demo Form page using:
file:///C:/Users/rchak/OneDrive/Documents/RajibWork/Learning/NewStudyNotes/AgenticAI/demo-form.html

Instantiate the following:

DemoFormPage

ExecutionEngineV3

RuntimeContext

Create test data for the scenario named:
TD_DemoForm_Populate_And_Verify_Table

The test data should:

Populate the following fields:

firstName = "Rajib"

subscribe = true

gender = genderMale

stateCity:

state = "CA"

city = "San Jose"

After populating the form fields, execute the verify phase.

The test should verify the following table condition:

In table "usersGrid":

Find the row where:
Name = "Alice"

Verify that:
Status TEXT_CONTAINS "ACTIVE"

All populate and verify operations must go through ExecutionEngineV3.

The test method must NOT:

Call driver.findElement

Instantiate TableActionExecutor directly

Use Assert.* manually

Perform manual table logic

All verification must be handled by the framework.

Table Test data Authoring Example:

Below is mandatory :
Table verification MUST be defined inside a "tables" array.

It MUST NOT be inside the "verify" block.

Correct structure:

"tables": [
{
"tableKey": "...",
"verify": [
{
"match": { ... },
"verify": {
"ColumnName": {
"type": "TEXT_EQUALS",
"expectedValue": "..."
}
}
}
]
}
]

Any other structure is invalid.
