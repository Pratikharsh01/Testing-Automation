package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import java.util.Set;


public class WebElementUtil {

    WebDriver driver;

    public WebElementUtil(WebDriver driver) {
        this.driver = driver;
    }

    public void insertText(By locator, String inputText, boolean... clearRequired) {

        waitForElementToAppear(locator);
        if (clearRequired.length == 0 || (clearRequired.length > 0 && clearRequired[0] == true)) {
            while (!driver.findElement(locator).getAttribute("value").isEmpty())
                driver.findElement(locator).sendKeys(Keys.BACK_SPACE);
        }
        driver.findElement(locator).sendKeys(inputText);
        waitForElementToHaveAttribute(locator, "value", inputText, 15);
    }

    public void sendKeyText(By locator, String inputText) {
        waitForElementToAppear(locator);
        driver.findElement(locator).sendKeys(inputText);
        waitForElementToHaveAttribute(locator, "value", inputText, 15);
    }

    public void sendKeyTextEmail(By locator, String inputText) {
        waitForElementToAppear(locator);
        driver.findElement(locator).sendKeys(inputText);

    }

    public void uploadFileUsingInputTag(By locator, String path) {
        driver.findElement(locator).sendKeys(path);

    }

    public void click(By locator) {
        waitForElementToAppear(locator);
        driver.findElement(locator).click();

    }

    public String getText(By locator) {
        waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    public void clickAnElementWaitForAnother(By clickLocater, By waitLocator) {
        for (int i = 0; i < 3; i++) {
            click(clickLocater);
            waitForElementToAppear(waitLocator);
            if (driver.findElement(waitLocator).isDisplayed())
                break;
        }
    }

    public void clickAnElementWaitForAnotherDisappear(By clickLocater, By waitLocator) {
        for (int i = 0; i < 3; i++) {
            click(clickLocater);
            waitForElementToDisappear(waitLocator);
            if (!driver.findElement(waitLocator).isDisplayed())
                break;
        }
    }

    public void waitForElementToAppear(By locator) {
        waitForElementToAppear(locator, 30);
    }

    public void waitForElementToAppear(By locator, long duration) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(duration));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForElementToDisappear(By locator) {
        waitForElementToDisappear(locator, 15);
    }

    public void waitForElementToHaveAttribute(By locator, String attributeName, String attributeValue, long duration) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(duration));
        wait.until(ExpectedConditions.attributeContains(locator, attributeName, attributeValue));

    }

    public void waitForElementToDisappear(By locator, long duration) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(duration));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void hardWait(int seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
        }
    }

    public int getElementsCollectionSize(By locator) {
        return driver.findElements(locator).size();
    }

    public void waitForFinalText(By locator) {
        String initialText = getText(locator);
        hardWait(5);
        String finalText = getText(locator);
        int retry = 0;
        while (!initialText.equalsIgnoreCase(finalText)) {
            initialText = finalText;
            hardWait(5);
            finalText = getText(locator);
            retry++;
            if (retry == 5)
                break;
        }
    }

    public void selectDropDown(String dropdownID, String option) {
        By itemTypeDropdown = By.xpath("//input[@id='" + dropdownID + "']//following::span[@class='ant-select-arrow']");
        By itemTypeSearch = By.xpath("//div[contains(@class,'ant-select-dropdown') and not(contains(@class,'hidden'))]//input[@class='ant-input']");
        By itemTypeSearchItemXpath = By.xpath("//div[@class='ant-select-tree-list']//span[text()='" + option + "']");
        clickAnElementWaitForAnother(itemTypeDropdown, itemTypeSearch);
        insertText(itemTypeSearch, option);
        click(itemTypeSearchItemXpath);
    }

    public void selectDropDownWithoutSearch(String dropdownID, String option) {
        By itemTypeDropdown = By.xpath("//input[@id='" + dropdownID + "']//ancestor::div[@class='ant-select-selector']|" +
                "//label[text()='" + dropdownID + "']//preceding-sibling::div//div[@class='ant-select-selector']");
        By itemTypeSearchItemXpath = By.xpath("//div[@class='ant-select-item-option-content' and text()='" + option + "']|" +
                "//span[@class='ant-select-tree-title' ]//span[text()='" + option + "']|" +
                "//div[contains(@class,'ant-select-dropdown')]//div[text()='" + option + "']|//div[@class='ant-select-item-option-content']//span[text()='" + option + "']");
        clickAnElementWaitForAnother(itemTypeDropdown, itemTypeSearchItemXpath);
        hardWait(1);
        clickUsingJS(itemTypeSearchItemXpath);
        hardWait(1);
    }


    public void clickAndOpenNewTab(By locator) {
        String currentWindow = driver.getWindowHandle();
        String newWindow = "";
        click(locator);
        Set<String> allWindows = driver.getWindowHandles();
        for (String windows : allWindows) {
            if (!windows.equalsIgnoreCase(currentWindow)) {
                newWindow = windows;
                break;
            }
        }

        driver.switchTo().window(newWindow);
    }

    public void doubleClick(By locator) {
        waitForElementToAppear(locator);
        Actions actions = new Actions(driver);
        WebElement elementLocator = driver.findElement(locator);
        actions.doubleClick(elementLocator).perform();
    }

    public void rightClick(By locator) {
        Actions actions = new Actions(driver);
        WebElement elementLocator = driver.findElement(locator);
        actions.contextClick(elementLocator).perform();
    }

    public void verifyToasterMessageForGateway(String message) {
        By toasterXpath = By.xpath("//div[normalize-space(text())='" + message + "']");
        waitForElementToAppear(toasterXpath);
        waitForElementToDisappear(toasterXpath);
    }


    public void selectByVisibleText(By locator, String visibleText) {
        Select dropDown = new Select(driver.findElement(locator));
        dropDown.selectByVisibleText(visibleText);
    }

    public boolean isAlertPresent() {
        try {
            Alert a = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
            if (a != null) {
                System.out.println("Alert is present");
                driver.switchTo().alert().accept();
                return true;
            } else {
                throw new Throwable();
            }
        } catch (Throwable e) {
            System.err.println("Alert isn't present!!");
            return false;
        }

    }
    public void scrollPage(){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,-250)");
    }


    public void clickUsingJS(By locator) {
        WebElement m = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", m);
    }

    public void clickAndSwitchToNewTab(By locator) {
        String currentWindowID = driver.getWindowHandle();
        String keyString = Keys.CONTROL + Keys.SHIFT.toString() + Keys.ENTER;
        driver.findElement(locator).sendKeys(keyString);
        hardWait(2);
        for (String winHandle : driver.getWindowHandles()) {
            if (winHandle != currentWindowID)
                driver.switchTo().window(winHandle);
        }
    }
}
