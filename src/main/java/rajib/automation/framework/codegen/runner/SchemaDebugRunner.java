package rajib.automation.framework.codegen.runner;


import rajib.automation.framework.codegen.loader.PageSchemaLoader;
import rajib.automation.framework.codegen.schema.CompositeFieldSchema;
import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.codegen.schema.PageSchema;
import rajib.automation.framework.codegen.validation.DefaultPageSchemaValidator;
import rajib.automation.framework.codegen.validation.PageSchemaValidator;

public class SchemaDebugRunner {

    public static void main(String[] args) {

        PageSchema schema =
                PageSchemaLoader.loadFromClasspath("agent/PracticeFormPage.json");

        // ✅ Validate schema
        PageSchemaValidator validator = new DefaultPageSchemaValidator();
        validator.validate(schema);

        System.out.println("✅ Schema validation passed");

        System.out.println("Page Name: " + schema.pageName());
        System.out.println("Framework: " + schema.framework());

        System.out.println("Composite Fields:");
        for (CompositeFieldSchema composite : schema.compositeFields()) {
            System.out.println("Composite: " + composite.key());
            for (FieldSchema component : composite.components()) {
                System.out.println("  - " + component.key()
                        + " [" + component.fieldType() + "] "
                        + component.locator().strategy()
                        + "=" + component.locator().value());
            }
        }

        System.out.println("Atomic Fields:");
        for (FieldSchema field : schema.fields()) {
            System.out.println("  - " + field.key()
                    + " [" + field.fieldType() + "] "
                    + field.locator().strategy()
                    + "=" + field.locator().value());
        }
    }
}
