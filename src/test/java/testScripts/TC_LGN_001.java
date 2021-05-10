package testScripts;

import org.testng.annotations.Test;
import pages.LoginPage;

/**
 * Author: Aravind
 * TC_LGN_001: Verify whether Registered Admin-User can able to access the
 * NopCommerce Admin LoginPage &  NopCommerce Admin Work-Space
 */

public class TC_LGN_001  extends  BaseTest{



    @Test
    public void TC_LGN_001(){

        LoginPage lp = new LoginPage(driver);
        lp.verifyLoginFlow("TC_LGN_001");
    }

}
