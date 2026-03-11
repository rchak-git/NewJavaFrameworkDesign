package rajib.automation.framework.td.model;



public record EscapeNode(
        String handler,
        String reason
) {
    public EscapeNode {
        if (handler == null || handler.isBlank()) {
            throw new IllegalArgumentException("Escape handler must not be null or blank");
        }
    }
}
