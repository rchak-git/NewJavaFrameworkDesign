package rajib.automation.framework.v3.round2.testdatamodels;

// In rajib.automation.framework.v3.round2.testdata.model

public record TestStepData(
        String fieldKey,
        String intent,
        Object value,
        Object expected,
        String validationType,
        String actionType
) {}