package rajib.automation.framework.td.model;



import java.util.Map;

public record TestDataDocument(
        PageNode page,
        FixtureNode fixture,
        EscapeNode escape,
        Map<String, FieldNode> fields,
        Map<String, TableNode> tables
) {}
