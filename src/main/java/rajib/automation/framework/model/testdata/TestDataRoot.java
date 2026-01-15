package rajib.automation.framework.model.testdata;

import java.util.List;

public record TestDataRoot(
        String page,
        List<TestCaseSpec> testCases
) {}
