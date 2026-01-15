package rajib.automation.framework.model.testdata;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record RowVerificationSpec(

        Map<String, String> match,

        @JsonProperty("assert")
        Map<String, AssertionSpec> assertThat
) {}
