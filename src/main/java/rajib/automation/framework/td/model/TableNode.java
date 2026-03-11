package rajib.automation.framework.td.model;


import rajib.automation.framework.intent.VerifySpec;

public record TableNode(
        VerifySpec verify,
        TableRowNode row
) {}
