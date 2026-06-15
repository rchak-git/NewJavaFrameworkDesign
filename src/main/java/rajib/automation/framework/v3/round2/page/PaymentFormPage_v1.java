package rajib.automation.framework.v3.round2.page;

import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;
import rajib.automation.framework.v3.round2.controls.ButtonControl;
import rajib.automation.framework.v3.round2.controls.SelectDropDownControl;
import rajib.automation.framework.v3.round2.controls.TextBoxControl;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import java.util.Map;

public class PaymentFormPage_v1 extends BasePageR2 {

    private final TextBoxControl customerName;
    private final TextBoxControl amount;
    private final SelectDropDownControl scenario;
    private final ButtonControl submit;

    public PaymentFormPage_v1(ElementResolver resolver, RuntimeContext runtimeContext) {
        super(resolver, runtimeContext);

        FieldSchema customerNameSchema = buildFieldSchema(
                "customerName",
                FieldType.TEXTBOX,
                "Customer Name",
                "main",
                "id",
                "customerName"
        );
        customerName = new TextBoxControl(customerNameSchema, resolver);
        registerControl("customerName", customerName);

        FieldSchema amountSchema = buildFieldSchema(
                "amount",
                FieldType.TEXTBOX,
                "Amount",
                "main",
                "id",
                "amount"
        );
        amount = new TextBoxControl(amountSchema, resolver);
        registerControl("amount", amount);

        FieldSchema scenarioSchema = buildFieldSchema(
                "scenario",
                FieldType.DROPDOWN,
                "Scenario",
                "main",
                "id",
                "scenario"
        );
        scenario = new SelectDropDownControl(scenarioSchema, resolver);
        registerControl("scenario", scenario);

        FieldSchema submitSchema = buildFieldSchema(
                "submit",
                FieldType.BUTTON,
                "Submit",
                "main",
                "css",
                "button[type='submit']"
        );
        submit = new ButtonControl(submitSchema, resolver);
        registerControl("submit", submit);
    }

    private FieldSchema buildFieldSchema(
            String key,
            FieldType fieldType,
            String logicalName,
            String locatorKey,
            String strategy,
            String value
    ) {
        LocatorSchema locatorSchema = new LocatorSchema();
        locatorSchema.setStrategy(strategy);
        locatorSchema.setValue(value);

        FieldSchema fieldSchema = new FieldSchema();
        fieldSchema.setKey(key);
        fieldSchema.setFieldType(fieldType);
        fieldSchema.setLogicalName(logicalName);
        fieldSchema.setLocators(Map.of(locatorKey, locatorSchema));

        return fieldSchema;
    }
}