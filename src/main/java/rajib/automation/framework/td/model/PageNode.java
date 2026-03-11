package rajib.automation.framework.td.model;


import rajib.automation.framework.intent.VerifySpec;

public record PageNode(
        VerifySpec verify,
        PageAction action
) {}
