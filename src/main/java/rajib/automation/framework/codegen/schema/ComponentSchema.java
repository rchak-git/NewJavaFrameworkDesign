package rajib.automation.framework.codegen.schema;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ComponentSchema models a repeating component block on a page (e.g., product card).
 *
 * MVP:
 * - Supports ONE identifier field (identifierField) if available.
 * - Index-based selection is supported by execution layer, not required in schema.
 *
 * Future:
 * - identifierFields (multi-key) can be introduced later without breaking this POJO
 *   by adding an additional optional property.
 */
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ComponentSchema(

        String key,

        @JsonAlias({"rootLocator"})
        LocatorSchema root,

        String resolutionStrategy,

        @JsonAlias({"identifierKey"})
        String identifierField,

        List<FieldSchema> fields

) {

    /**
     * Canonical constructor – auto-normalizes resolutionStrategy
     */
    public ComponentSchema {
        // Normalize resolutionStrategy if null
        if (resolutionStrategy == null || resolutionStrategy.isBlank()) {
            resolutionStrategy =
                    (identifierField != null && !identifierField.isBlank())
                            ? "IDENTIFIED"
                            : "SINGLE";
        }
    }

    /**
     * Backward-compatible constructor (old generator support)
     */
    public ComponentSchema(
            String key,
            LocatorSchema root,
            String identifierField,
            List<FieldSchema> fields
    ) {
        this(
                key,
                root,
                (identifierField != null && !identifierField.isBlank())
                        ? "IDENTIFIED"
                        : "SINGLE",
                identifierField,
                fields
        );
    }

    public boolean isIdentified() {
        return "IDENTIFIED".equalsIgnoreCase(resolutionStrategy);
    }

    public boolean isSingle() {
        return "SINGLE".equalsIgnoreCase(resolutionStrategy);
    }

    public FieldSchema fieldOrThrow(String fieldKey) {
        return fields.stream()
                .filter(f -> f.key().equals(fieldKey))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Component field not found. component=" + key + ", field=" + fieldKey
                ));
    }
}
