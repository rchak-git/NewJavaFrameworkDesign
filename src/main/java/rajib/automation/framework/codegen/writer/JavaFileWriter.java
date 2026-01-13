package rajib.automation.framework.codegen.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaFileWriter {

    public static void write(Path filePath, String javaSource) {
        try {
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, javaSource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Java source file", e);
        }
    }
}
