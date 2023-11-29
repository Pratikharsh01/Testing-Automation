package mkUIpagefactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;
import utils.WebElementUtil;

import static org.testng.Assert.assertEquals;

public class AdminPage {
    WebDriver driver;
    WebElementUtil weu;


    By MobileNo = By.xpath("//input[contains(@name,'mobileno')]");

    By otpSignInBtn = By.xpath("//div//button[@id='btnSubmit']");
    By password = By.xpath("//div//input[@id='userPassword']");
    By logoutBtn = By.xpath("//div//ul//button[@id='logout']");
    By userName = By.xpath("//div//ul//h6[@id='userName']");
    By masterData = By.xpath("//a//span[text()='MASTER DATA']");
    By distributor = By.xpath("//a//span[text()='DISTRIBUTORS']");
    By add_DistributorCompany = By.xpath("//a//span[text()='ADD DISTRIBUTOR COMPANY']");
    By view_DistributorCompanies = By.xpath("//a//span[text()='VIEW DISTRIBUTOR COMPANIES']");
    By distributorName = By.xpath("//div//input[@id='txtInDistributorCompanyName']");
    By distributorAddress = By.xpath("//div//input[@id='txtInDistributorCompanyAddressLineI']");
    By selectCountry = By.xpath("//div//select[@id='ddlCountries']");
    By distributorPostalCode =By.xpath("//div//input[@id='txtInDistributorCompanyPinCode']");
    By distributorPassword = By.xpath("//div//input[@id='txtInDistributorCompanyPassword']");
    By selectState = By.xpath("//div//select[@id='ddlStates']");
    By selectDistrict = By.xpath("//div//select[@id='ddlDistricts']");
    By phoneCountryCode = By.xpath("//div//input[@id='txtInMobileCountryCode']");
    By addButton =By.xpath("//div//button[@id='btnAddUpdate']");
    By successfullyMessage = By.xpath("//div//p[contains(text(),'Successfully')]");
    By searchCriteria = By.xpath("//div//select[@id='ddlSearchCriteria']");
    By countries = By.xpath("//div//select[@id='ddlCountries']");
    By searchText = By.xpath("//div//input[@id='txtInSearchText']");
    By rowsPerPage = By.xpath("//div//select[@id='ddlRowsPerPage']");
    By sortOn = By.xpath("//div//select[@id='ddlSortOption']");
    By searchButton = By.xpath("//td//button[text()='Search']");
    By distributorCompanyFound = By.xpath("(//tr//td[text()='Banty Kumar'])[1]");
    By deleteButton = By.xpath("//td//button[text()='Delete']");
    By confirmDelete = By.xpath("//div//button[@onClick='deleteRecord()']");
    public AdminPage() {
        this.driver = DriverManager.getCurrentDriver();
        weu = new WebElementUtil(driver);

    }

    public void adminLoginWithOTP() {
        driver.get("https://conqudel.ecomsaas.click/sign-in");
        weu.hardWait(10);
        weu.insertText(MobileNo, "+918789280700");
        weu.click(otpSignInBtn);
        weu.insertText(password, "softwareadmin");
        weu.hardWait(5);
    }
    public void addDistributorCompany(){
        weu.click(masterData);
        weu.click(distributor);
        weu.click(add_DistributorCompany);
        weu.hardWait(3);
        weu.insertText(distributorName,"Banty Kumar");
        weu.insertText(distributorAddress,"Mtwri, Hzbg");
        weu.selectByVisibleText(selectCountry,"India");
        weu.hardWait(2);
        weu.selectByVisibleText(selectState,"Jharkhand");
        weu.selectByVisibleText(selectDistrict,"Hazaribagh");
        weu.insertText(distributorPostalCode,"825301");
        weu.insertText(distributorPassword,"Test@123");
        weu.insertText(phoneCountryCode,"+91");
        weu.click(addButton);
        weu.hardWait(5);
        weu.scrollPage();
        String successMessage = weu.getText(successfullyMessage);
        assertEquals(successMessage, "Successfully added the distributor company");
        System.out.println(successMessage);
    }
    public void searchViewDeleteDistributorCompany()
    {
        weu.hardWait(5);
        weu.click(masterData);
        weu.click(distributor);
        weu.click(view_DistributorCompanies);
        weu.selectByVisibleText(searchCriteria,"Location Country");
        weu.selectByVisibleText(countries,"India");
        weu.insertText(searchText,"Banty");
        weu.selectByVisibleText(rowsPerPage,"5");
        weu.selectByVisibleText(sortOn,"Distributor Name");
        weu.hardWait(3);
        weu.click(searchButton);
        weu.hardWait(2);

        String distCompany = weu.getText(distributorCompanyFound);
        assertEquals(distCompany,"Banty Kumar");
        System.out.println("Distributor Company is Available.");
        weu.hardWait(5);

        weu.click(deleteButton);
        weu.click(confirmDelete);
        weu.hardWait(3);
        String successfullyDelete = weu.getText(successfullyMessage);
        assertEquals(successfullyDelete,"Successfully deleted the distributor company");
        System.out.println(successfullyDelete);
        weu.hardWait(2);
    }
    public void verifyUserName(){
        String adminText = weu.getText(userName);
        assertEquals(adminText, "softwareadmin");
        System.out.println("Successfully log-in as Software Admin: " +adminText);
    }
    public void logout(){
        weu.click(logoutBtn);
        weu.hardWait(3);
        System.out.println("Successfully Logged out.");

    }
}
