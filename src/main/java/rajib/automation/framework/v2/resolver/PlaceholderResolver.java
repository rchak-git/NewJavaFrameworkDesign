package rajib.automation.framework.v2.resolver;

import rajib.automation.framework.v2.context.RuntimeContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlaceholderResolver {

    /**
     * Resolves placeholders in test data using the RuntimeContext.
     * Supports nested maps and lists.
     */
    public static Map<String, Object> resolve(
            Map<String, Object> rawData,
            RuntimeContext context
    ) {
        Map<String, Object> resolved = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : rawData.entrySet()) {
            resolved.put(entry.getKey(), resolveValue(entry.getValue(), context));
        }

        return resolved;
    }

    private static Object resolveValue(Object value, RuntimeContext context) {
        if (value instanceof String s) {
            return resolveString(s, context);
        }

        if (value instanceof Map<?, ?> map) {
            Map<String, Object> resolvedMap = new LinkedHashMap<>();
            map.forEach((k, v) -> resolvedMap.put(String.valueOf(k), resolveValue(v, context)));
            return resolvedMap;
        }

        if (value instanceof List<?> list) {
            return list.stream()
                    .map(v -> resolveValue(v, context))
                    .toList();
        }

        return value;
    }

    private static Object resolveString(String value, RuntimeContext context) {
        if (isPlaceholder(value)) {
            String key = extractKey(value);

            if (!context.contains(key)) {
                throw new IllegalArgumentException(
                        "Unresolved placeholder '" + value + "'. No value found in RuntimeContext."
                );
            }

            return context.get(key);
        }

        return value;
    }

    private static boolean isPlaceholder(String value) {
        return value != null && value.startsWith("${") && value.endsWith("}") && value.length() > 3;
    }

    private static String extractKey(String placeholder) {
        return placeholder.substring(2, placeholder.length() - 1);
    }
}
