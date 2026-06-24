/*package rajib.automation.framework.v3.round2.page;

import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.LocatorSchema;
import rajib.automation.framework.v3.round2.controls.*;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

import java.util.Map;

public class PracticeFormPage_v2 extends BasePageR2 {

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
        private final TableControl submissionDetailsTable;

        public PracticeFormPage_v2(ElementResolver resolver, RuntimeContext runtimeContext) {
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
                                "male", new LocatorSchema("id", "gender-radio-1"),
                                "female", new LocatorSchema("id", "gender-radio-2"),
                                "other", new LocatorSchema("id", "gender-radio-3")
                        )
                );

                FieldSchema userNumberSchema = buildFieldSchema(
                        "userNumber",
                        FieldType.TEXTBOX,
                        "Mobile Number",
                        "main",
                        "id",
                        "userNumber"
                );

                FieldSchema dateOfBirthInputSchema = buildFieldSchema(
                        "dateOfBirthInput",
                        FieldType.DATEBOX,
                        "Date Of Birth",
                        "main",
                        "id",
                        "dateOfBirthInput"
                );

                FieldSchema subjectsInputSchema = buildFieldSchema(
                        "subjectsInput",
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
                                "sports", new LocatorSchema("id", "hobbies-checkbox-1"),
                                "reading", new LocatorSchema("id", "hobbies-checkbox-2"),
                                "music", new LocatorSchema("id", "hobbies-checkbox-3")
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

                FieldSchema submitSchema = buildFieldSchema(
                        "submit",
                        FieldType.BUTTON,
                        "Submit",
                        "main",
                        "id",
                        "submit"
                );

                FieldSchema submissionDetailsTableSchema = buildTableSchema(
                        "submissionDetailsTable",
                        "Submission Details",
                        "css",
                        ".modal-content .table-responsive table",
                        "tbody tr",
                        "td"
                );

                firstName = new TextBoxControl(firstNameSchema, resolver);
                registerControl("firstName", firstName);

                lastName = new TextBoxControl(lastNameSchema, resolver);
                registerControl("lastName", lastName);

                userEmail = new TextBoxControl(userEmailSchema, resolver);
                registerControl("userEmail", userEmail);

                gender = new RadioGroupControl(genderSchema, resolver);
                registerControl("gender", gender);

                mobileNumber = new TextBoxControl(userNumberSchema, resolver);
                registerControl("userNumber", mobileNumber);

                dateOfBirth = new DateControl(dateOfBirthInputSchema, resolver);
                registerControl("dateOfBirthInput", dateOfBirth);

                subjects = new TextBoxControl(subjectsInputSchema, resolver);
                registerControl("subjectsInput", subjects);

                hobbies = new CheckboxControl(hobbiesSchema, resolver);
                registerControl("hobbies", hobbies);

                currentAddress = new TextBoxControl(currentAddressSchema, resolver);
                registerControl("currentAddress", currentAddress);

                submit = new ButtonControl(submitSchema, resolver);
                registerControl("submit", submit);

                submissionDetailsTable = new TableControl(submissionDetailsTableSchema, resolver);
                registerControl("submissionDetailsTable", submissionDetailsTable);
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

        private FieldSchema buildTableSchema(
                String key,
                String logicalName,
                String strategy,
                String tableValue,
                String rowValue,
                String cellValue
        ) {
                LocatorSchema tableLocator = new LocatorSchema();
                tableLocator.setStrategy(strategy);
                tableLocator.setValue(tableValue);

                LocatorSchema rowLocator = new LocatorSchema();
                rowLocator.setStrategy(strategy);
                rowLocator.setValue(rowValue);

                LocatorSchema cellLocator = new LocatorSchema();
                cellLocator.setStrategy(strategy);
                cellLocator.setValue(cellValue);

                FieldSchema tableSchema = new FieldSchema();
                tableSchema.setKey(key);
                tableSchema.setLogicalName(logicalName);
                tableSchema.setFieldType(FieldType.TABLE);
                tableSchema.setLocators(Map.of(
                        "table", tableLocator,
                        "row", rowLocator,
                        "cell", cellLocator
                ));

                return tableSchema;
        }
}

 */