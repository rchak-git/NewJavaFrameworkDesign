package rajib.automation.framework.td.model;



import rajib.automation.framework.intent.VerifySpec;

import java.util.Map;

public record TableRowNode(
        Map<String, Object> match,
        Map<String, Object> populate,
        VerifySpec verify,
        RowAction action
) {}
