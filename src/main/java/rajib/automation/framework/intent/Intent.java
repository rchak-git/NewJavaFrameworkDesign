package rajib.automation.framework.intent;

import java.util.Optional;

/**
 * Normalized intent for a single field.
 *
 * - populate: what value (if any) should be populated
 * - verify: how / what should be verified
 */
public record Intent(
        Optional<Object> populate,
        Optional<VerifySpec> verify
) {
    public static Intent empty() {
        return new Intent(Optional.empty(), Optional.empty());
    }
}
