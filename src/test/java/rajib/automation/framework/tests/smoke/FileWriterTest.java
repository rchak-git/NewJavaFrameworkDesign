package rajib.automation.framework.tests.smoke;

import org.testng.annotations.Test;
import rajib.automation.framework.utils.FileUtils;

public class FileWriterTest {

    @Test
    public void testWriteToFile() {
        String path = "target/output/sample.txt";

        FileUtils fu = new FileUtils();
        fu.writeText(path, "Hello Rajib - file writing successful and directory gets created!");

        System.out.println("Data written to: " + path);
    }
}
