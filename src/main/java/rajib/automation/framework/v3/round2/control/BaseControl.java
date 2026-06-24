package rajib.automation.framework.v3.round2.control;

import rajib.automation.framework.v3.round2.ai.schema.models.BaseSchema;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

public abstract class BaseControl implements Control {

    protected final BaseSchema schema;
    protected final ElementResolver resolver;

    protected BaseControl(BaseSchema schema,
                          ElementResolver resolver) {
        this.schema = schema;
        this.resolver = resolver;
    }

    protected String key() {
        return schema.getKey();
    }

    public abstract void populate(ControlCommand command);

    public abstract void verify(ControlCommand command);

    public void doAction(ControlCommand command) {
        throw new UnsupportedOperationException("Custom action not supported: " + command.getAction());
    }
}