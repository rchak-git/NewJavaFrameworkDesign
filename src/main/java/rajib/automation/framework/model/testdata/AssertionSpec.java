package rajib.automation.framework.model.testdata;

import com.fasterxml.jackson.annotation.JsonCreator;

public record AssertionSpec(
        String type,
        String value
) {

    @JsonCreator
    public static AssertionSpec fromString(String value) {
        return new AssertionSpec("EQUALS", value);
    }

    public String effectiveType() {
        return (type == null || type.isBlank()) ? "EQUALS" : type;
    }
}
