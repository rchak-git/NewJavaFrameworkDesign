Create a new Java file named PaymentFormDemo.java in the demo package.

Return the entire file as ONE single code block only.
Do not split the code into multiple blocks.
Do not place any code outside the block.
Do not add explanations before or after the block.
Do not add markdown text outside the block.

The code block must contain:
- package declaration
- imports
- class declaration
- fields
- constructor
- helper methods
- closing brace

Target path:
src/main/java/rajib/automation/framework/v3/round2/page/demo/PaymentFormDemo.java

Package:
rajib.automation.framework.v3.round2.page.demo

Class name:
PaymentFormDemo

Use the existing framework style and generate a compile-ready file.

Framework details:
- extend BasePageR2
- constructor parameters: ElementResolver resolver, RuntimeContext runtimeContext
- use FieldSchema and LocatorSchema
- use TextBoxControl, SelectDropDownControl, ButtonControl
- infer all required imports

Schema:
{
"fields": [
{
"key": "customerName",
"fieldType": "TEXTBOX",
"logicalName": "Customer Name",
"locators": {
"main": {
"strategy": "id",
"value": "customerName"
}
}
},
{
"key": "amount",
"fieldType": "TEXTBOX",
"logicalName": "Amount",
"locators": {
"main": {
"strategy": "id",
"value": "amount"
}
}
},
{
"key": "scenario",
"fieldType": "DROPDOWN",
"logicalName": "Scenario",
"locators": {
"main": {
"strategy": "id",
"value": "scenario"
}
}
},
{
"key": "submit",
"fieldType": "BUTTON",
"logicalName": "Submit",
"locators": {
"main": {
"strategy": "css",
"value": "button[type='submit']"
}
}
}
],
"tables": []
}

Rules:
- Use only the schema above
- Do not generate tables
- Do not generate legacy structures
- Do not invent extra controls
- Do not produce partial output
- Do not output anything outside the single code block