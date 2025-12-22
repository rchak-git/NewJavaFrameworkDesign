package rajib.automation.framework.tests.Miscelleneous;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class FileUpload extends BaseTest {

    @Test
    public void uploadFile() throws URISyntaxException {
       driver.get("http://omayo.blogspot.com");

       URL url  = this.getClass().getClassLoader().getResource("sample.txt");
       File inputFile = new File(url.toURI());
       driver.findElement(By.id("uploadfile")).sendKeys(inputFile.toString());

        System.out.println("Wait here");


    }

    @Test
    public void downloadFile() throws URISyntaxException {
        driver.get("https://omayo.blogspot.com/p/page7.html");

        driver.findElement(By.linkText("ZIP file")).click();
        String downloadPath = System.getProperty("user.dir") ;//+ "/DownloadDemo-master.zip";
        File file = waitForFileDownload(downloadPath,"DownloadDemo-master.zip",10);
        System.out.println("Wait here");


    }

    public static File waitForFileDownload(String downloadDir, String fileName, int timeoutSeconds) {
        File dir = new File(downloadDir);
        File file = new File(downloadDir + File.separator + fileName);

        int elapsed = 0;
        while (elapsed < timeoutSeconds) {
            if (file.exists() && file.length() > 0) {
                return file;
            }
            try {
                Thread.sleep(500);   // small polling, NOT a blind wait
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            elapsed += 1;
        }

        throw new RuntimeException("File NOT downloaded within timeout: " + fileName);
    }
}
