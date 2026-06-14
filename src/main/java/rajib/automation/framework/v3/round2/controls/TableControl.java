package rajib.automation.framework.v3.round2.controls;

import rajib.automation.framework.v3.round2.ai.schema.models.FieldSchema;
import rajib.automation.framework.v3.round2.ai.schema.models.TableSchema;
import rajib.automation.framework.v3.round2.control.BaseControl;
import rajib.automation.framework.v3.round2.control.ControlCommand;
import rajib.automation.framework.v3.round2.resolver.ElementResolver;

public class TableControl extends BaseControl {

    public TableControl(FieldSchema schema, ElementResolver resolver) {
        super(schema, resolver);
    }

    @Override
    public void populate(ControlCommand command) {
        throw new UnsupportedOperationException(
                "Table population not supported yet"
        );
    }

    @Override
    public void verify(ControlCommand command) {
        // empty for now
    }

    @Override
    public Object read() {
        throw new UnsupportedOperationException(
                "Table read not implemented yet"
        );
    }
}