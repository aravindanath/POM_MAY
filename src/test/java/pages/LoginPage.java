package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import utilites.JavaUtils;

import java.util.HashMap;
import java.util.List;

public class LoginPage  extends BasePage{


    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
    }

    @FindBy(id = "Email")
    WebElement emailField;

    @FindBy(id = "Password")
    WebElement passwordField;

    @FindBy(xpath = "//button[text()='Log in']")
    WebElement loginButton;

    @FindBy(xpath = "//h1[contains(text(),'Dashboard')]")
    WebElement dashboardTitle;

    @FindBy(id="RememberMe")
    WebElement rememberMeCheckBox;

    @FindBy(xpath = "//h1[text()='Admin area demo']")
    WebElement adminTitle;


    HashMap<String, String> lp;


    public void verifyLoginFlow(String tcID){
        lp = JavaUtils.readExcelData("Login",tcID);
        waitForElement(2000);
        emailField.clear();
        emailField.sendKeys(lp.get("USERNAME"));
        waitForElement(2000);
        passwordField.clear();
        passwordField.sendKeys(lp.get("PASSWORD"));
        rememberMeCheckBox.click();
        waitForElement(1000);
        loginButton.click();
        assertTitle(dashboardTitle,"Dashboard");
        System.out.println(driver.getCurrentUrl());
    }


    public void verifyAdminPage(){
        assertTitle(adminTitle,"Admin area demo");
    }



}
