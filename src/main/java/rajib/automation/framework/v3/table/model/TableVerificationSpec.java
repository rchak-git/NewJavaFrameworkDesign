package rajib.automation.framework.v3.table.model;

import java.util.List;

public record TableVerificationSpec(
        String tableKey,
        String id,
        List<RowVerifySpec> verify
) {}