package rajib.automation.framework.resolution;

import rajib.automation.framework.intent.Intent;

/**
 * Final decision for a field after intent resolution.
 */
public record ResolvedIntent(
        boolean shouldPopulate,
        boolean shouldVerify,
        Intent intent
) {}
