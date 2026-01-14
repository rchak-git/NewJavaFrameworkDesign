package rajib.automation.framework.tables.verifier;

import java.util.List;
import java.util.Map;

public record TableVerificationSpec(
        String tableKey,
        List<RowVerificationSpec> verify
) { }
