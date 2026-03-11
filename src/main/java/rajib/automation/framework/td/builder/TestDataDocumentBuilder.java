package rajib.automation.framework.td.builder;


import rajib.automation.framework.intent.Intent;
import rajib.automation.framework.td.model.*;

import java.util.HashMap;
import java.util.Map;

public final class TestDataDocumentBuilder {

    private TestDataDocumentBuilder() {
        // utility class
    }

    public static TestDataDocument from(Map<String, Intent> intents) {

        if (intents == null || intents.isEmpty()) {
            return new TestDataDocument(null, null, null, Map.of(), Map.of());
        }

        PageNode pageNode = null;
        FixtureNode fixtureNode = null;
        EscapeNode escapeNode = null;

        Map<String, FieldNode> fieldNodes = new HashMap<>();
        Map<String, TableNode> tableNodes = new HashMap<>();

        for (Map.Entry<String, Intent> entry : intents.entrySet()) {

            String key = entry.getKey();
            Intent intent = entry.getValue();

            if ("page".equalsIgnoreCase(key)) {
                pageNode = new PageNode(
                        intent.verify().orElse(null),
                        null
                );
            }

            else if ("fixture".equalsIgnoreCase(key)) {
                fixtureNode = new FixtureNode(
                        intent.populate()
                                .map(Object::toString)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Fixture name missing"))
                );
            }

            else if ("escape".equalsIgnoreCase(key)) {
                escapeNode = new EscapeNode(
                        intent.populate()
                                .map(Object::toString)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Escape handler missing")),
                        null
                );
            }

            else {
                // Default → FIELD
                fieldNodes.put(
                        key,
                        new FieldNode(
                                intent.populate().orElse(null),
                                intent.verify().orElse(null),
                                null
                        )
                );
            }
        }

        return new TestDataDocument(
                pageNode,
                fixtureNode,
                escapeNode,
                fieldNodes,
                tableNodes
        );
    }
}
