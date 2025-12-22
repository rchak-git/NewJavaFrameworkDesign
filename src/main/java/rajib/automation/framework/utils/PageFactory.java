package rajib.automation.framework.utils;

import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

public class PageFactory {

    private static WebDriver driver;

    public PageFactory(WebDriver driver) {
        this.driver = driver;
    }

    public static <T> T  getPage(Class<T> pageClass) {
        Supplier<T> supplier = () -> {
            try {
                return pageClass
                       // .getDeclaredConstructor(WebDriver.class)
                        .getConstructor()
                        .newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Unable to create page: " + pageClass.getName(), e);
            }
        };

        return supplier.get();
    }
}
