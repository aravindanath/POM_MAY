package testScripts;

import org.testng.annotations.Test;
import pages.LoginPage;

public class TC_OPN_01 extends BaseTest{


    @Test
    public void TC_OPN_01(){

        LoginPage lp = new LoginPage(driver);
        lp.verifyAdminPage();

    }
}
