package rajib.automation.framework.pages.login;

import rajib.automation.framework.utils.MyFindBy;

import java.lang.reflect.Field;

public class MiniPageFactory {

    public static void main(String[] args) {

        MiniPageFactory.initElements(LoginPageFactory.class);
    }
    public static void initElements(Class<?> pageClass) {



        System.out.println("Scanning fields in: " + pageClass.getSimpleName());

        for (Field field : pageClass.getDeclaredFields()) {

            if (field.isAnnotationPresent(MyFindBy.class)) {

                MyFindBy annotation = field.getAnnotation(MyFindBy.class);

                System.out.println("Field: " + field.getName()
                        + " → Locator ID: " + annotation.id());
            }
        }
    }
}
