package rajib.automation.framework.tables.verifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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


