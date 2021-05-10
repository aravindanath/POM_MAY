package testScripts;

import org.testng.annotations.Test;
import pages.DashBoard;
import pages.LoginPage;

import java.util.Date;

/**
 * Author: Aravind
 * TC_LGN_002: Verify whether Registered Admin-User can able to access the 'Catalog' tab in NopCommerce Work-Space.
 *
 */

public class TC_LGN_002 extends  BaseTest{



    @Test
    public void TC_LGN_002(){

        LoginPage lp = new LoginPage(driver);
        lp.verifyLoginFlow("TC_LGN_002");
        DashBoard db = new DashBoard(driver);
        db.verifyCatalogMenu();

    }

}
