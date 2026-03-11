package rajib.automation.framework.pages.rahul;

import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.codegen.schema.LocatorSchema;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.enums.FieldType;
import java.util.*;
import rajib.automation.framework.codegen.schema.ComponentSchema;
import rajib.automation.framework.codegen.schema.FieldSchema;


public class MycartsPage extends BasePage {

    public MycartsPage() {

        initSchemas();

        pageFields.put("continueShopping",
                new PageField(
                        "continueShopping",
                        "css",
                        "button[routerlink='/dashboard']",
                        FieldType.ACTION,
                        "continueShopping"
                )
        );

        pageFields.put("subtotalValue",
                new PageField(
                        "subtotalValue",
                        "xpath",
                        "//li[span[normalize-space()='Subtotal']]/span[@class='value']",
                        FieldType.STATIC_TEXT,
                        "subtotalValue"
                )
        );

        pageFields.put("totalValue",
                new PageField(
                        "totalValue",
                        "xpath",
                        "//li[span[normalize-space()='Total']]/span[@class='value']",
                        FieldType.STATIC_TEXT,
                        "totalValue"
                )
        );

        pageFields.put("checkout",
                new PageField(
                        "checkout",
                        "xpath",
                        "//button[normalize-space()='Checkout']",
                        FieldType.ACTION,
                        "checkout"
                )
        );

        componentSchemas.put("cartItem",
                new ComponentSchema(
                        "cartItem",
                        new LocatorSchema("css", "li.items"),
                        "productTitle",
                        List.of(
                                new FieldSchema(
                                        "productTitle",
                                        FieldType.STATIC_TEXT,
                                        null,
                                        new LocatorSchema("xpath", ".//h3")
                                ),
                                new FieldSchema(
                                        "itemNumber",
                                        FieldType.STATIC_TEXT,
                                        null,
                                        new LocatorSchema("xpath", ".//p[contains(@class,'itemNumber')]")
                                ),
                                new FieldSchema(
                                        "stockStatus",
                                        FieldType.STATIC_TEXT,
                                        null,
                                        new LocatorSchema("xpath", ".//p[contains(@class,'stockStatus')]")
                                ),
                                new FieldSchema(
                                        "price",
                                        FieldType.STATIC_TEXT,
                                        null,
                                        new LocatorSchema("xpath", ".//div[contains(@class,'prodTotal')]//p")
                                ),
                                new FieldSchema(
                                        "buyNow",
                                        FieldType.ACTION,
                                        null,
                                        new LocatorSchema("xpath", ".//button[normalize-space()='Buy Now']")
                                ),
                                new FieldSchema(
                                        "remove",
                                        FieldType.ACTION,
                                        null,
                                        new LocatorSchema("xpath", ".//button[contains(@class,'btn-danger')]")
                                )
                        )
                )
        );

    }

    public boolean isProductInCart(String productTitle) {
        return componentExists("cartItem", productTitle);
    }

    protected boolean componentExists(String componentKey, String identifierValue) {
        ComponentSchema schema = componentSchemas.get(componentKey);

        try {
            resolveComponentRoot(schema, identifierValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
