package rajib.automation.framework.pages;



import rajib.automation.framework.annotations.PageTables;
import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.codegen.schema.TableColumnSchema;
import rajib.automation.framework.codegen.schema.TableSchema;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.enums.FieldType;
import java.util.*;

public class DemoFormPage extends BasePage {


    @PageTables
    private final List<TableSchema> tables = List.of(

            // ---- Standard HTML table ----
            new TableSchema(
                    "usersTable",
                    new LocatorSchema("id", "usersTable"),
                    new LocatorSchema("css", "tbody tr"),
                    new LocatorSchema("css", "td"),
                    List.of(
                            new TableColumnSchema("UserId", 0, FieldType.TEXT),
                            new TableColumnSchema("Name", 1, FieldType.TEXT),
                            new TableColumnSchema("Status", 2, FieldType.TEXT)
                    )
            ),

            // ---- Custom div-based grid ----
            new TableSchema(
                    "usersGrid",
                    new LocatorSchema("id", "usersGrid"),
                    new LocatorSchema("css", ".row:not(.header)"),
                    new LocatorSchema("css", ".cell"),
                    List.of(
                            new TableColumnSchema("UserId", 0, FieldType.TEXT),
                            new TableColumnSchema("Name", 1, FieldType.TEXT),
                            new TableColumnSchema("Status", 2, FieldType.TEXT)
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

        pageFields.put("submit",
                new PageField(
                        "submit",
                        "id",
                        "submitBtn",
                        FieldType.ACTION,
                        "submit"
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

        compositePageFields.put("stateCity", new ArrayList<>());
        compositePageFields.get("stateCity").add(pageFields.get("state"));
        compositePageFields.get("stateCity").add(pageFields.get("city"));

    }
}


