package rajib.automation.framework.model.testdata;



import java.util.List;

public record TableVerificationSpec(
        String tableKey,
        String id,
        List<RowVerificationSpec> verify
) {}
