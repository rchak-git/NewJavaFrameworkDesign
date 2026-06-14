package rajib.automation.framework.v3.round2.page;

import java.util.Map;


import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;
import rajib.automation.framework.v3.round2.controls.*;

public class PracticeFormPage extends BasePageR2 {

    private final TextBoxControl firstName;
    private final TextBoxControl lastName;
    private final TextBoxControl userEmail;
    private final RadioGroupControl gender;
    private final TextBoxControl mobileNumber;
    private final DateControl dateOfBirth;
    private final TextBoxControl subjects;
    private final CheckboxControl hobbies;
    private final TextBoxControl currentAddress;

    private final ButtonControl submit;

    public PracticeFormPage(ElementResolver resolver, RuntimeContext runtimeContext) {

        super(resolver, runtimeContext);

        FieldSchema firstNameSchema = buildFieldSchema(
                "firstName",
                FieldType.TEXTBOX,
                "First Name",
                "main",
                "id",
                "firstName"
        );

        FieldSchema lastNameSchema = buildFieldSchema(
                "lastName",
                FieldType.TEXTBOX,
                "Last Name",
                "main",
                "id",
                "lastName"
        );

        FieldSchema userEmailSchema = buildFieldSchema(
                "userEmail",
                FieldType.TEXTBOX,
                "User Email",
                "main",
                "id",
                "userEmail"
        );

        FieldSchema genderSchema = new FieldSchema(
                "gender",
                FieldType.RADIOGROUP,
                "Gender",
                Map.of(
                        "male",   new LocatorSchema("id", "gender-radio-1"),
                        "female", new LocatorSchema("id", "gender-radio-2"),
                        "other",  new LocatorSchema("id", "gender-radio-3")
                )
        );

        FieldSchema mobileNumberSchema = buildFieldSchema(
                "mobileNumber",
                FieldType.TEXTBOX,
                "Mobile Number",
                "main",
                "id",
                "userNumber"
        );

        FieldSchema dateOfBirthSchema = buildFieldSchema(
                "dateOfBirth",
                FieldType.DATEBOX,
                "Date Of Birth",
                "main",
                "id",
                "dateOfBirthInput"
        );

        FieldSchema subjectsSchema = buildFieldSchema(
                "subjects",
                FieldType.TEXTBOX,
                "Subjects",
                "main",
                "id",
                "subjectsInput"
        );

        FieldSchema hobbiesSchema = new FieldSchema(
                "hobbies",
                FieldType.CHECKBOX,
                "Hobbies",
                Map.of(
                        "sports",      new LocatorSchema("id", "hobbies-checkbox-1"),
                        "reading",     new LocatorSchema("id", "hobbies-checkbox-2"),
                        "music",       new LocatorSchema("id", "hobbies-checkbox-3")
                )
        );

        FieldSchema currentAddressSchema = buildFieldSchema(
                "currentAddress",
                FieldType.TEXTBOX,
                "Current Address",
                "main",
                "id",
                "currentAddress"
        );

        FieldSchema stateSchema = buildFieldSchema(
                "state",
                FieldType.DROPDOWN,
                "State",
                "main",
                "id",
                "state"
        );

        FieldSchema citySchema = buildFieldSchema(
                "city",
                FieldType.DROPDOWN,
                "City",
                "main",
                "id",
                "city"
        );

        FieldSchema submitSchema = buildFieldSchema(
                "submit",
                FieldType.BUTTON,
                "Submit",
                "main",
                "id",
                "submit"
        );

        firstName = new TextBoxControl(firstNameSchema, resolver);
        registerControl("firstName", firstName);

        lastName = new TextBoxControl(lastNameSchema, resolver);
        registerControl("lastName", lastName);

        userEmail = new TextBoxControl(userEmailSchema, resolver);
        registerControl("userEmail", userEmail);

        gender = new RadioGroupControl(genderSchema, resolver);
        registerControl("gender", gender);

        mobileNumber = new TextBoxControl(mobileNumberSchema, resolver);
        registerControl("mobileNumber", mobileNumber);

        dateOfBirth = new DateControl(dateOfBirthSchema, resolver);
        registerControl("dateOfBirth", dateOfBirth);

        subjects = new TextBoxControl(subjectsSchema, resolver);
        registerControl("subjects", subjects);

        hobbies = new CheckboxControl(hobbiesSchema, resolver);
        registerControl("hobbies", hobbies);

        currentAddress = new TextBoxControl(currentAddressSchema, resolver);
        registerControl("currentAddress", currentAddress);



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