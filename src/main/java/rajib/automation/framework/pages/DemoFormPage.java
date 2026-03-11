package rajib.automation.framework.pages;



import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.codegen.schema.TableColumnSchema;
import rajib.automation.framework.codegen.schema.TableSchema;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.enums.FieldType;
import java.util.*;

import rajib.automation.framework.annotations.PageTables;

public class DemoFormPage extends BasePage {

    @PageTables
    private final List<TableSchema> tables = List.of(
            new TableSchema(
                    "usersTable",
                    new LocatorSchema("id", "usersTable"),
                    new LocatorSchema("css", "#usersTable tbody tr"),
                    new LocatorSchema("css", "td"),
                    List.of(
                            new TableColumnSchema("UserId", 0, FieldType.STATIC_TEXT),
                            new TableColumnSchema("Name", 1, FieldType.STATIC_TEXT),
                            new TableColumnSchema("Status", 2, FieldType.STATIC_TEXT)
                    )
            ),
            new TableSchema(
                    "usersGrid",
                    new LocatorSchema("id", "usersGrid"),
                    new LocatorSchema("css", "#usersGrid .row:not(.header)"),
                    new LocatorSchema("css", ".cell"),
                    List.of(
                            new TableColumnSchema("UserId", 0, FieldType.STATIC_TEXT),
                            new TableColumnSchema("Name", 1, FieldType.STATIC_TEXT),
                            new TableColumnSchema("Status", 2, FieldType.ACTION)
                    )
            )
    );

    public DemoFormPage() {

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

        pageFields.put("subscribe",
                new PageField(
                        "subscribe",
                        "id",
                        "subscribe",
                        FieldType.CHECKBOX,
                        "subscribe"
                )
        );

        pageFields.put("submitBtn",
                new PageField(
                        "submitBtn",
                        "id",
                        "submitBtn",
                        FieldType.ACTION,
                        "submitBtn"
                )
        );

        pageFields.put("genderMale",
                new PageField(
                        "genderMale",
                        "id",
                        "genderMale",
                        FieldType.RADIO,
                        "genderMale"
                )
        );

        pageFields.put("genderFemale",
                new PageField(
                        "genderFemale",
                        "id",
                        "genderFemale",
                        FieldType.RADIO,
                        "genderFemale"
                )
        );

        pageFields.put("state",
                new PageField(
                        "state",
                        "id",
                        "state",
                        FieldType.DROPDOWN,
                        "state"
                )
        );

        pageFields.put("city",
                new PageField(
                        "city",
                        "id",
                        "city",
                        FieldType.DROPDOWN,
                        "city"
                )
        );

        compositePageFields.put("gender", new ArrayList<>());
        compositePageFields.get("gender").add(pageFields.get("genderMale"));
        compositePageFields.get("gender").add(pageFields.get("genderFemale"));

        compositePageFields.put("stateCity", new ArrayList<>());
        compositePageFields.get("stateCity").add(pageFields.get("state"));
        compositePageFields.get("stateCity").add(pageFields.get("city"));

    }
}
