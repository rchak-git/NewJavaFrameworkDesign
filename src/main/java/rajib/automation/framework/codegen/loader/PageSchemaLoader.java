package rajib.automation.framework.codegen.loader;



import com.fasterxml.jackson.databind.ObjectMapper;
import rajib.automation.framework.codegen.schema.PageSchema;

import java.io.InputStream;

public class PageSchemaLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static PageSchema loadFromClasspath(String resourcePath) {
        try (InputStream is =
                     PageSchemaLoader.class
                             .getClassLoader()
                             .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new RuntimeException("JSON file not found: " + resourcePath);
            }

            return mapper.readValue(is, PageSchema.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load PageSchema JSON", e);
        }
    }
}
