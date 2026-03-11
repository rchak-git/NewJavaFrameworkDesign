package rajib.automation.framework.v2.resolver;

import rajib.automation.framework.v2.context.RuntimeContext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlaceholderResolver {

    /**
     * Resolves placeholders in a TestData map using the ExecutionContext.
     * Supports nested maps and lists.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> resolve(
            Map<String, Object> rawData,
            RuntimeContext context
    ) {

        Map<String, Object> resolved = new LinkedHashMap<>();;

        for (Map.Entry<String, Object> entry : rawData.entrySet()) {
            resolved.put(
                    entry.getKey(),
                    resolveValue(entry.getValue(), context)
            );
        }

        return resolved;
    }

    private static Object resolveValue(Object value, RuntimeContext context) {

        if (value instanceof String s) {
            return resolveString(s, context);
        }

        if (value instanceof Map<?, ?> map) {
            Map<String, Object> resolvedMap = new HashMap<>();
            map.forEach((k, v) ->
                    resolvedMap.put(
                            String.valueOf(k),
                            resolveValue(v, context)
                    )
            );
            return resolvedMap;
        }

        if (value instanceof List<?> list) {
            return list.stream()
                    .map(v -> resolveValue(v, context))
                    .toList();
        }

        // numbers, booleans, null → unchanged
        return value;
    }

    private static Object resolveString(String value, RuntimeContext context) {

        if (isPlaceholder(value)) {
            String key = extractKey(value);

            if (!context.contains(key)) {
                throw new RuntimeException(
                        "Unresolved placeholder '" + value +
                                "'. No value found in ExecutionContext."
                );
            }

            return context.get(key);
        }

        return value;
    }

    private static boolean isPlaceholder(String value) {
        return value.startsWith("<")
                && value.endsWith(">")
                && value.length() > 2;
    }

    private static String extractKey(String placeholder) {
        return placeholder.substring(1, placeholder.length() - 1);
    }
}
