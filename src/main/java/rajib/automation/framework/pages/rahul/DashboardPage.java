package rajib.automation.framework.pages.rahul;

import org.openqa.selenium.WebElement;
import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.enums.FieldType;
import java.util.*;
import rajib.automation.framework.codegen.schema.ComponentSchema;
import rajib.automation.framework.codegen.schema.FieldSchema;


public class DashboardPage extends BasePage {

    public DashboardPage() {

        initSchemas();

        pageFields.put("check",
                new PageField(
                        "check",
                        "id",
                        "check",
                        FieldType.CHECKBOX,
                        "check"
                )
        );

        pageFields.put("productName",
                new PageField(
                        "productName",
                        "css",
                        "input[formcontrolname='productName']",
                        FieldType.TEXTBOX,
                        "productName"
                )
        );

        pageFields.put("minPrice",
                new PageField(
                        "minPrice",
                        "css",
                        "input[formcontrolname='minPrice']",
                        FieldType.TEXTBOX,
                        "minPrice"
                )
        );

        pageFields.put("maxPrice",
                new PageField(
                        "maxPrice",
                        "css",
                        "input[formcontrolname='maxPrice']",
                        FieldType.TEXTBOX,
                        "maxPrice"
                )
        );

        pageFields.put("productName_2",
                new PageField(
                        "productName_2",
                        "css",
                        "section#sidebar input[formcontrolname='productName']",
                        FieldType.TEXTBOX,
                        "productName_2"
                )
        );

        pageFields.put("minPrice_2",
                new PageField(
                        "minPrice_2",
                        "css",
                        "section#sidebar input[formcontrolname='minPrice']",
                        FieldType.TEXTBOX,
                        "minPrice_2"
                )
        );

        pageFields.put("maxPrice_2",
                new PageField(
                        "maxPrice_2",
                        "css",
                        "section#sidebar input[formcontrolname='maxPrice']",
                        FieldType.TEXTBOX,
                        "maxPrice_2"
                )
        );

        pageFields.put("res",
                new PageField(
                        "res",
                        "id",
                        "res",
                        FieldType.STATIC_TEXT,
                        "res"
                )
        );

        componentSchemas.put("productCard",
                new ComponentSchema(
                        "productCard",
                        new LocatorSchema("css", "div.card-body"),
                        "title",
                        List.of(
                                new FieldSchema(
                                        "title",
                                        FieldType.STATIC_TEXT,
                                        null,
                                        new LocatorSchema("css", "h5 b")
                                ),
                                new FieldSchema(
                                        "price",
                                        FieldType.STATIC_TEXT,
                                        null,
                                        new LocatorSchema("css", "div.text-muted")
                                ),
                                new FieldSchema(
                                        "view",
                                        FieldType.ACTION,
                                        null,
                                        new LocatorSchema("xpath", ".//button[contains(.,'View')]")
                                ),
                                new FieldSchema(
                                        "addToCart",
                                        FieldType.ACTION,
                                        null,
                                        new LocatorSchema("xpath", ".//button[contains(.,'Add To Cart')]")
                                )
                        )
                )
        );

    }

    public void addProductToCart(String productTitle) {

        // 1️⃣ Resolve correct component root based on identifier
        waitForSpinnerToDisappear();
        WebElement root = resolveComponentRoot("productCard", productTitle);

        // 2️⃣ Perform action inside that component
        waitForSpinnerToDisappear();
        performComponentAction("productCard", root, "addToCart");
    }
}
