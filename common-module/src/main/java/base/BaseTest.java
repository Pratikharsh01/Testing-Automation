package base;

import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeSuite;
import utils.PropertyReaderUtil;

import java.io.File;

public class BaseTest {

    @BeforeSuite
    public static void setupPrerequisite()
    {

        String basePath = new File(System.getProperty("user.dir")).getParent()+System.getProperty("file.separator")+"common-module/";
        System.out.println("Current dirctories "+basePath);
        PropertyConfigurator.configure(basePath+"src/main/java/resources/log4j.properties");
        PropertyReaderUtil.readProperties(basePath+"src/main/java/resources/application.properties");
    }
}
