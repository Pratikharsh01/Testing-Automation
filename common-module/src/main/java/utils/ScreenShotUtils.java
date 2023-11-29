package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ScreenShotUtils {

    public static void takeScreenshot() {
        String screenshotLoc = "target/screenshot/";
        try {
            WebDriver webdriver = DriverManager.getCurrentDriver();
            TakesScreenshot scrShot = (TakesScreenshot) webdriver;
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            String screenshotPath = screenshotLoc + new Date().getTime() + ".jpeg";
            File DestFile = new File(screenshotPath);

            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception ex) {
            System.out.println("Unable to locate");
        }
    }

    public static byte[] takeAllureScreenshot() {
        String screenshotLoc = "target/screenshot/";
        byte[] SrcFile = null;
        try {
            WebDriver webdriver = DriverManager.getCurrentDriver();
            TakesScreenshot scrShot = (TakesScreenshot) webdriver;
            SrcFile = scrShot.getScreenshotAs(OutputType.BYTES);

        } catch (Exception ex) {
            System.out.println("Unable to locate");
        }
        return SrcFile;
    }


}
