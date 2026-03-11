package rajib.automation.framework.routing;


import rajib.automation.framework.td.model.ExecutionTarget;
/*

 */
public record RoutingResult(
        ExecutionTarget target,
        String targetKey,
        Object payload
) {
    public RoutingResult {
        if (target == null) {
            throw new IllegalArgumentException("ExecutionTarget must not be null");
        }
        if (targetKey == null || targetKey.isBlank()) {
            throw new IllegalArgumentException("targetKey must not be null or blank");
        }
        if (payload == null) {
            throw new IllegalArgumentException("payload must not be null");
        }
    }
}
