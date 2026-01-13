package rajib.automation.framework.pages;


import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.model.PageField;

import java.util.List;

public class PracticeFormPage extends BasePage {

    public PracticeFormPage() {

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

        pageFields.put("mobileNumber",
                new PageField(
                        "mobileNumber",
                        "id",
                        "userNumber",
                        FieldType.TEXTBOX,
                        "mobileNumber"
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

        pageFields.put("submit",
                new PageField(
                        "submit",
                        "id",
                        "submit",
                        FieldType.ACTION,
                        "submit"
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

        compositePageFields.put("stateCity", List.of(
                pageFields.get("state"),
                pageFields.get("city")
        ));

    }
}
