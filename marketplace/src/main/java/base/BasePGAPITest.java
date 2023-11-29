package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import utils.ConfigUtil;
import utils.EnvironmentUtil;
import utils.RestAPIUtils;

public class BasePGAPITest extends BaseAPITest{

        @BeforeClass
        public static void setUp()
        {
            String countryToTest = System.getProperty("country");
            ConfigUtil.updateData(countryToTest);
            EnvironmentUtil.updateData(countryToTest);
        }

        @AfterMethod
        public static void tearUp()
        {
            RestAPIUtils.clearRequest();;
        }

}
