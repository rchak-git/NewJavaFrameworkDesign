package rajib.automation.framework.v3.round2.control;

import org.openqa.selenium.WebElement;

import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

public abstract class BaseControl implements Control {

    protected final FieldSchema schema;
    protected final ElementResolver resolver;

    protected BaseControl(FieldSchema schema,
                          ElementResolver resolver) {
        this.schema = schema;
        this.resolver = resolver;
    }

    protected WebElement resolveElement() {
        return resolver.resolve(schema);
    }

    protected String key() {
        return schema.key;
    }

    // ✅ ADD THESE (if not present)
    public abstract void populate(ControlCommand command);

    public abstract void verify(ControlCommand command);

   public void doAction(ControlCommand command) {
        throw new UnsupportedOperationException("Custom action not supported: " + command.getAction());
    }


}