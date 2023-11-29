package testcaseui;

import base.BasePGUITest;
import org.testng.annotations.Test;
import mkUIpagefactory.AdminPage;

public class AdminTest extends BasePGUITest {
    AdminPage adminPage;

    @Test
    public void Admin_Test_Method(){
        adminPage = new AdminPage();
        adminPage.adminLoginWithOTP();
        adminPage.verifyUserName();
//        adminPage.addDistributorCompany();
//        adminPage.searchViewDeleteDistributorCompany();
        adminPage.logout();
    }
}
