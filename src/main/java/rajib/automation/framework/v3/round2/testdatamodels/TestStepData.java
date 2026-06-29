package rajib.automation.framework.v3.round2.testdatamodels;

public record TestStepData(
        String fieldKey,
        String intent,
        Object value,
        Object expected,
        Object matchBy,
        String validationType,
        String actionType,
        String populationType
) {}