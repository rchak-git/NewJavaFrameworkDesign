package rajib.automation.framework.loader;


import com.fasterxml.jackson.databind.ObjectMapper;
import rajib.automation.framework.model.testdata.*;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class TableVerificationLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TableVerificationLoader() {
        // utility
    }

    public static TableVerificationSpec load(
            File testDataFile,
            String testCaseName,
            String id
    ) {

        TestDataRoot root = readRoot(testDataFile);

        TestCaseSpec testCase = root.testCases().stream()
                .filter(tc -> Objects.equals(tc.name(), testCaseName))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Test case not found: " + testCaseName
                        )
                );

        if (testCase.tables() == null || testCase.tables().isEmpty()) {
            throw new IllegalStateException(
                    "No table verifications defined for test case: " + testCaseName
            );
        }

        List<TableVerificationSpec> matches =
                testCase.tables().stream()
                        .filter(t -> Objects.equals(t.id(), id))
                        .collect(Collectors.toList());

        if (matches.isEmpty()) {
            throw new IllegalStateException(
                    "No table verification with id '" + id +
                            "' found in test case: " + testCaseName
            );
        }

        if (matches.size() > 1) {
            throw new IllegalStateException(
                    "Duplicate table verification id '" + id +
                            "' found in test case: " + testCaseName
            );
        }

        TableVerificationSpec spec = matches.get(0);

        if (spec.verify() == null || spec.verify().isEmpty()) {
            throw new IllegalStateException(
                    "Verification '" + id +
                            "' contains no row assertions"
            );
        }

        return spec;
    }

    private static TestDataRoot readRoot(File file) {
        try {
            return MAPPER.readValue(file, TestDataRoot.class);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to load test data file: " + file.getAbsolutePath(),
                    e
            );
        }
    }
}
