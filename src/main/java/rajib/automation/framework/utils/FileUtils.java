package rajib.automation.framework.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public void writeText(String filePath, String text) {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            java.nio.file.Files.createDirectories(path.getParent()); // auto create dirs

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
                bw.write(text);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + filePath, e);
        }
    }
}
