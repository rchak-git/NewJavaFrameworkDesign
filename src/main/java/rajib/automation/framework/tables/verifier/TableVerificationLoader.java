package rajib.automation.framework.tables.verifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.File;
import java.io.IOException;

public class TableVerificationLoader {

    private static final ObjectMapper mapper =
            new ObjectMapper()
                    .configure(
                            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                            false
                    );

    public static TableVerificationSpec load(File jsonFile) {
        try {
            return mapper.readValue(jsonFile, TableVerificationSpec.class);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to load table verification JSON: " + jsonFile.getAbsolutePath(),
                    e
            );
        }
    }
}
