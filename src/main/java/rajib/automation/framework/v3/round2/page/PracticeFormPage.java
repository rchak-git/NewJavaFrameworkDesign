package rajib.automation.framework.v3.round2.page;



import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.context.RuntimeContextHolder;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;
import rajib.automation.framework.v3.round2.controls.ButtonControl;
import rajib.automation.framework.v3.round2.controls.RadioGroupControl;
import rajib.automation.framework.v3.round2.controls.TextBoxControl;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import java.util.HashMap;
import java.util.Map;

public class PracticeFormPage extends BasePageR2 {

    // Hold a reference (optional if always going via BasePageR2)
    protected final RuntimeContext context;

    public PracticeFormPage(ElementResolver resolver, RuntimeContext context) {
        super(resolver, context);
        this.context = context;

        registerControl("firstName", new TextBoxControl(
                new FieldSchema("firstName", FieldType.TEXTBOX, "First Name",
                        mapOf("main", new LocatorSchema("id", "firstName"))),
                resolver
        ));

        registerControl("lastName", new TextBoxControl(
                new FieldSchema("lastName", FieldType.TEXTBOX, "Last Name",
                        mapOf("main", new LocatorSchema("id", "lastName"))),
                resolver
        ));

        registerControl("userEmail", new TextBoxControl(
                new FieldSchema("userEmail", FieldType.TEXTBOX, "Email",
                        mapOf("main", new LocatorSchema("id", "userEmail"))),
                resolver
        ));

        registerControl("userNumber", new TextBoxControl(
                new FieldSchema("userNumber", FieldType.TEXTBOX, "Mobile",
                        mapOf("main", new LocatorSchema("id", "userNumber"))),
                resolver
        ));

        registerControl("subjectsInput", new TextBoxControl(
                new FieldSchema("subjectsInput", FieldType.TEXTBOX, "Subjects",
                        mapOf("main", new LocatorSchema("id", "subjectsInput"))),
                resolver
        ));

        registerControl("currentAddress", new TextBoxControl(
                new FieldSchema("currentAddress", FieldType.TEXTBOX, "Current Address",
                        mapOf("main", new LocatorSchema("id", "currentAddress"))),
                resolver
        ));

        registerControl("submit", new ButtonControl(
                new FieldSchema(
                        "submit",
                        FieldType.BUTTON,
                        "Submit",
                        mapOf("main", new LocatorSchema("id", "submit")) // Adjust locator as appropriate!
                ),
                resolver
        ));
        // --- Registering the Gender RadioGroup control ---
        registerControl("gender", new RadioGroupControl(
                new FieldSchema(
                        "gender",
                        FieldType.RADIOGROUP, // Use RADIOGROUP in your updated enum
                        "Gender",
                        Map.of(
                                "male",   new LocatorSchema("id", "gender-radio-1"),
                                "female", new LocatorSchema("id", "gender-radio-2"),
                                "other",  new LocatorSchema("id", "gender-radio-3")
                        )
                ),
                resolver
        ));
    }

    // Utility method stays
    private Map<String, LocatorSchema> mapOf(String k, LocatorSchema v) {
        Map<String, LocatorSchema> m = new HashMap<>();
        m.put(k, v);
        return m;
    }
}