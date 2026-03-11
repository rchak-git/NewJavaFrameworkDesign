package rajib.automation.framework.v2.runtime;

import rajib.automation.framework.v2.context.RuntimeContext;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class RuntimeValueResolver {

    // 🔥 Generator registry
    private static final Map<String, Supplier<String>> GENERATORS = Map.of(
            "RUNTIME_EMAIL", RuntimeValueResolver::generateEmail,
            "RUNTIME_UUID", () -> UUID.randomUUID().toString(),
            "RUNTIME_TIMESTAMP", () -> String.valueOf(System.currentTimeMillis())
    );

    public String resolve(String rawValue, RuntimeContext context) {

        if (rawValue == null) {
            return null;
        }

        // Rule 1: Runtime generation
        if (rawValue.startsWith("RUNTIME_")) {
            return resolveRuntimeValue(rawValue, context);
        }

        // Rule 2: Context lookup
        if (rawValue.startsWith("CTX.")) {
            return resolveFromContext(rawValue, context);
        }

        // Rule 3: Literal
        return rawValue;
    }

    private String resolveRuntimeValue(String key, RuntimeContext context) {

        // If already generated, reuse
        if (context.contains(key)) {
            return context.getString(key);
        }

        Supplier<String> generator = GENERATORS.get(key);

        if (generator == null) {
            throw new IllegalArgumentException(
                    "No runtime generator registered for key: " + key
            );
        }

        String generatedValue = generator.get();
        context.put(key, generatedValue);
        return generatedValue;
    }

    private String resolveFromContext(String rawValue, RuntimeContext context) {

        String[] parts = rawValue.split("\\.", 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid CTX reference format: " + rawValue
            );
        }

        String contextKey = parts[1];

        if (!context.contains(contextKey)) {
            throw new IllegalStateException(
                    "RuntimeContext does not contain key: " + contextKey
            );
        }

        return context.getString(contextKey);
    }

    private static String generateEmail() {
        return "user_" + System.currentTimeMillis() + "@testmail.com";
    }
}
