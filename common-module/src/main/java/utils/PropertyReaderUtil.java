package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReaderUtil {
    public static Properties auto = new Properties();

    public static void readProperties(String pathApplication) {
        auto = new Properties();
        try {
            FileInputStream file = new FileInputStream(pathApplication);
            auto.load(file);
            for (String name : auto.stringPropertyNames()) {
                System.setProperty(name, System.getProperty(name, auto.getProperty(name)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("unable to read file");
        }
    }
}
