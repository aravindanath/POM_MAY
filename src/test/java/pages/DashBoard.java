package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class DashBoard  extends BasePage{


    public DashBoard(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
    }




    @FindBy(xpath = "(//p[contains(text(),'Catalog')])[1]")
    WebElement catalogMenu;

    @FindBys(@FindBy(xpath ="(//p[contains(text(),'Catalog')])[1]//following::ul[1]/li/a/p" ))
    List<WebElement> catalogList;





    public void verifyCatalogMenu(){
        waitForElement(2000);
        assertElementEnable(catalogMenu);
        catalogMenu.click();
        waitForElement(2000);
        System.out.println("Total no of catalog items: "+catalogList.size());
        for(WebElement ele : catalogList){
            System.out.println(ele.getText());
        }
    }



}
