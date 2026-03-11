package rajib.automation.framework.normalization.model;



import java.util.Map;

public record PopulateFieldsPayload(
        Map<String, Object> fields
) {}
