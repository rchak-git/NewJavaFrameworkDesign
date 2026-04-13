package rajib.automation.framework.v3.round2.ai.schema.models;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SchemaLoader {
    public static PageSchema loadSchema(String resourcePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        // For records/Java 16+, you may also need:  mapper.findAndRegisterModules();
        return mapper.readValue(
                Files.newInputStream(Paths.get(resourcePath)), PageSchema.class
        );
    }

    public static void main(String[] args) throws Exception {
        String resourcePath = "src/main/resources/ai/genschema/demoqa_practice_form_textbox.schema.json";
        PageSchema pageSchema = loadSchema(resourcePath);
        List<FieldSchema> schemas = pageSchema.getFields();
        System.out.println("Loaded " + schemas.size() + " fields from: " + resourcePath);
        for (FieldSchema fs : schemas) {
            System.out.println(fs.key + " [" + fs.fieldType + "] �� " + fs.locators);
        }
    }
}