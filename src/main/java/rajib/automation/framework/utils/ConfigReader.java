package rajib.automation.framework.utils;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties prop = new Properties();

    // Loads properties from classpath (e.g., /resources/config/config.properties)
    public static Properties loadProperties() {
        if (prop.isEmpty()) {
            try (InputStream input = ConfigReader.class.getClassLoader()
                    .getResourceAsStream("config/config.properties")) {

                if (input == null) {
                    throw new RuntimeException("❌ config/config.properties file not found in classpath!");
                }
                prop.load(input);

            } catch (IOException e) {
                throw new RuntimeException("❌ Failed to load config.properties: " + e.getMessage());
            }
        }
        return prop;
    }

    // Returns the property value for a given key
    public static String getProperty(String key) {
        if (prop.isEmpty()) {
            loadProperties();  // auto-load if not already loaded
        }
        return prop.getProperty(key);
    }

    public static int getIntProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing property: " + key);
        }
        return Integer.parseInt(value);
    }
}
