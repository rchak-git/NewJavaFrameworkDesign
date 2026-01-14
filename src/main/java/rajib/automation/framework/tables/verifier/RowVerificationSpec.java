package rajib.automation.framework.tables.verifier;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record RowVerificationSpec(
        Map<String, String> match,          // row identification criteria (WT-4.2 exact)
        @JsonProperty("assert")
        Map<String, AssertionSpec> assertThat // column assertions for that row
) { }
