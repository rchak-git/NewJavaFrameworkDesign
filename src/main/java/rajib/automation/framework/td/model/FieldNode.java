package rajib.automation.framework.td.model;


import rajib.automation.framework.intent.VerifySpec;

public record FieldNode(
        Object populate,
        VerifySpec verify,
        FieldAction action
) {}
