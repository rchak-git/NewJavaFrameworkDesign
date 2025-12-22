package rajib.automation.framework.tests.Miscelleneous;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import rajib.automation.framework.base.BaseTest;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class FrameExample extends BaseTest {

    @Test
    public void exampleFrame()  {
       driver.get("https://the-internet-herokuapp.com/iFrame");

        System.out.println("Wait here");


    }


}
