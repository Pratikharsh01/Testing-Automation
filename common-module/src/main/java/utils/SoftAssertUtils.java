package utils;

import org.testng.asserts.SoftAssert;

public class SoftAssertUtils {
    static SoftAssert softAssert = new SoftAssert();

    public static void addAssertion(boolean flag) {
        softAssert.assertTrue(flag);
    }

    public static void assertNow() {
        softAssert.assertAll();
        softAssert = new SoftAssert();
    }
}
