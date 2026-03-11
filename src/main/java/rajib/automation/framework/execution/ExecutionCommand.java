package rajib.automation.framework.execution;


import rajib.automation.framework.td.model.ExecutionTarget;
import rajib.automation.framework.td.model.Phase;

public record ExecutionCommand(
        Phase phase,
        ExecutionTarget target,
        String targetKey,
        Object payload
) {

    public ExecutionCommand {
        if (phase == null) {
            throw new IllegalArgumentException("phase must not be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }
        if (targetKey == null || targetKey.isBlank()) {
            throw new IllegalArgumentException("targetKey must not be null or blank");
        }
        if (payload == null) {
            throw new IllegalArgumentException("payload must not be null");
        }
    }
}
