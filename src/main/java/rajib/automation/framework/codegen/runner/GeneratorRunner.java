package rajib.automation.framework.codegen.runner;

import rajib.automation.framework.codegen.generator.PageClassGenerator;
import rajib.automation.framework.codegen.loader.PageSchemaLoader;
import rajib.automation.framework.codegen.schema.PageSchema;
import rajib.automation.framework.codegen.writer.JavaFileWriter;

import java.nio.file.Path;

public class GeneratorRunner {

    public static void main(String[] args) {

        PageSchema schema =
                PageSchemaLoader.loadFromClasspath("agent/DemoFormSchema.json");

        PageClassGenerator generator = new PageClassGenerator();
        String javaSource = generator.generate(schema);

        JavaFileWriter.write(
                Path.of("generated/" + schema.pageName() + ".java"),
                javaSource
        );

        System.out.println(
                schema.pageName() + ".java generated successfully"
        );
    }
}

