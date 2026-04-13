package rajib.automation.framework.v3.round2.testdatamodels;

public record TestScenario(
        String scenario,
        java.util.List<TestStepData> steps
) {}