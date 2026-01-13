package rajib.automation.framework.introspection;

import rajib.automation.framework.annotations.PageTables;
import rajib.automation.framework.codegen.schema.TableSchema;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class PageTableExtractor {

    private PageTableExtractor() {
        // utility class
    }

    /**
     * Extracts table schemas declared on a page using @PageTables.
     * WT-1.3 only: discovery, no validation, no registration.
     */
    public static List<TableSchema> extract(Object page) {
        System.out.println("DEBUG: PageTableExtractor.extract() CALLED for "
                + page.getClass().getSimpleName());

        if (page == null) return List.of();

        List<TableSchema> tables = new ArrayList<>();
        Class<?> clazz = page.getClass();

        // 1️⃣ Field-based discovery
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(PageTables.class)) continue;

            field.setAccessible(true);
            Object value = readField(field, page);
            System.out.println(
                    "DEBUG @PageTables field '" + field.getName() +
                            "' value class = " + (value == null ? "null" : value.getClass()) +
                            ", value = " + value
            );
            tables.addAll(asTableSchemas(value, field.getName()));
        }

        // 2️⃣ Method-based discovery (optional but future-safe)
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(PageTables.class)) continue;
            if (method.getParameterCount() != 0) continue;

            method.setAccessible(true);
            Object value = invokeMethod(method, page);
            tables.addAll(asTableSchemas(value, method.getName() + "()"));
        }

        return tables;
    }

    private static Object readField(Field field, Object page) {
        try {
            return field.get(page);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot read @PageTables field: " + field.getName(), e);
        }
    }

    private static Object invokeMethod(Method method, Object page) {
        try {
            return method.invoke(page);
        } catch (Exception e) {
            throw new RuntimeException("Cannot invoke @PageTables method: " + method.getName(), e);
        }
    }

    private static List<TableSchema> asTableSchemas(Object value, String source) {
        if (value == null) return List.of();

        if (value instanceof TableSchema schema) {
            return List.of(schema);
        }

        if (value instanceof List<?> list) {
            List<TableSchema> out = new ArrayList<>();
            for (Object o : list) {
                if (!(o instanceof TableSchema)) {
                    throw new IllegalArgumentException(
                            "@PageTables must return TableSchema or List<TableSchema>. Found: "
                                    + o.getClass().getSimpleName() + " at " + source
                    );
                }
                out.add((TableSchema) o);
            }
            return out;
        }

        throw new IllegalArgumentException(
                "@PageTables must return TableSchema or List<TableSchema>. Found: "
                        + value.getClass().getSimpleName() + " at " + source
        );
    }
}
