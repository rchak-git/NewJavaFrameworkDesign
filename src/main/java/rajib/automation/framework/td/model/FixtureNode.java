package rajib.automation.framework.td.model;



public record FixtureNode(
        String name
) {
    public FixtureNode {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Fixture name must not be null or blank");
        }
    }
}
