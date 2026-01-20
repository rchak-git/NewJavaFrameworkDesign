package rajib.automation.framework.pages;

import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.enums.FieldType;
import java.util.*;

public class RegistrationPage extends BasePage {

    public RegistrationPage() {

        initSchemas();

        pageFields.put("firstName",
                new PageField(
                        "firstName",
                        "id",
                        "firstName",
                        FieldType.TEXTBOX,
                        "firstName"
                )
        );

        pageFields.put("lastName",
                new PageField(
                        "lastName",
                        "id",
                        "lastName",
                        FieldType.TEXTBOX,
                        "lastName"
                )
        );

        pageFields.put("email",
                new PageField(
                        "email",
                        "id",
                        "userEmail",
                        FieldType.TEXTBOX,
                        "email"
                )
        );

        pageFields.put("genderMale",
                new PageField(
                        "genderMale",
                        "id",
                        "gender-radio-1",
                        FieldType.RADIO,
                        "genderMale"
                )
        );

        pageFields.put("genderFemale",
                new PageField(
                        "genderFemale",
                        "id",
                        "gender-radio-2",
                        FieldType.RADIO,
                        "genderFemale"
                )
        );

        pageFields.put("genderOther",
                new PageField(
                        "genderOther",
                        "id",
                        "gender-radio-3",
                        FieldType.RADIO,
                        "genderOther"
                )
        );

        pageFields.put("mobileNumber",
                new PageField(
                        "mobileNumber",
                        "id",
                        "userNumber",
                        FieldType.TEXTBOX,
                        "mobileNumber"
                )
        );

        pageFields.put("dateOfBirth",
                new PageField(
                        "dateOfBirth",
                        "id",
                        "dateOfBirthInput",
                        FieldType.TEXTBOX,
                        "dateOfBirth"
                )
        );

        pageFields.put("subjects",
                new PageField(
                        "subjects",
                        "id",
                        "subjectsInput",
                        FieldType.DROPDOWN,
                        "subjects"
                )
        );

        pageFields.put("hobbySports",
                new PageField(
                        "hobbySports",
                        "id",
                        "hobbies-checkbox-1",
                        FieldType.CHECKBOX,
                        "hobbySports"
                )
        );

        pageFields.put("hobbyReading",
                new PageField(
                        "hobbyReading",
                        "id",
                        "hobbies-checkbox-2",
                        FieldType.CHECKBOX,
                        "hobbyReading"
                )
        );

        pageFields.put("hobbyMusic",
                new PageField(
                        "hobbyMusic",
                        "id",
                        "hobbies-checkbox-3",
                        FieldType.CHECKBOX,
                        "hobbyMusic"
                )
        );

        pageFields.put("currentAddress",
                new PageField(
                        "currentAddress",
                        "id",
                        "currentAddress",
                        FieldType.TEXTBOX,
                        "currentAddress"
                )
        );

        pageFields.put("state",
                new PageField(
                        "state",
                        "id",
                        "react-select-3-input",
                        FieldType.DROPDOWN,
                        "state"
                )
        );

        pageFields.put("city",
                new PageField(
                        "city",
                        "id",
                        "react-select-4-input",
                        FieldType.DROPDOWN,
                        "city"
                )
        );

        pageFields.put("submit",
                new PageField(
                        "submit",
                        "id",
                        "submit",
                        FieldType.ACTION,
                        "submit"
                )
        );

    }
}
