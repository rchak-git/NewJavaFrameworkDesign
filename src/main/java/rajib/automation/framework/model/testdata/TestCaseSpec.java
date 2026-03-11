package rajib.automation.framework.model.testdata;



import rajib.automation.framework.v3.table.model.TableVerificationSpec;

import java.util.List;
import java.util.Map;

public record TestCaseSpec(
        String name,
        Map<String, Object> data,
        List<TableVerificationSpec> tables
) {}
