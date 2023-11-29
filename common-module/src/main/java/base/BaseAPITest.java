package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import utils.RestAPIUtils;


public class BaseAPITest extends BaseTest {

    @BeforeClass
    public static void setUp()
    {

    }

    @AfterMethod
    public static void tearUp()
    {
        RestAPIUtils.clearRequest();;
    }


}
