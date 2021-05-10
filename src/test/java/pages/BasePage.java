package pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BasePage {

    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * @param driver
     * @param target
     * @Author Arvind
     * mouseHover is method using action class for mouse hover on the element.
     */


    public static void mouseHover(WebDriver driver, WebElement target) {
        Actions act = new Actions(driver);
        act.moveToElement(target).build().perform();
    }


    public static void click(WebDriver driver, WebElement target) {
        Actions act = new Actions(driver);
        act.click(target).build().perform();
    }


    public static void rightclick(WebDriver driver, WebElement target) {
        Actions act = new Actions(driver);
        act.contextClick(target).build().perform();
    }

    public static void dragNDrop(WebDriver driver, WebElement source, WebElement target) {
        Actions act = new Actions(driver);
        act.dragAndDrop(source, target).build().perform();
    }


    public static void acceptAlert(WebDriver driver) {
        Alert alt = driver.switchTo().alert();
        System.out.println("ALert Title: " + alt.getText());
        alt.accept();
    }


    public static void dismissAlert(WebDriver driver) {
        Alert alt = driver.switchTo().alert();
        System.out.println("ALert Title: " + alt.getText());
        alt.dismiss();
    }


    public static void acceptAlert(WebDriver driver, String text) {
        Alert alt = driver.switchTo().alert();
        System.out.println("ALert Title: " + alt.getText());
        alt.sendKeys(text);
        alt.accept();
    }

    public static void dismissAlert(WebDriver driver, String text) {
        Alert alt = driver.switchTo().alert();
        System.out.println("ALert Title: " + alt.getText());
        alt.sendKeys(text);
        alt.dismiss();
    }


    public static void selectBYVisibleText(WebElement element, String text) {
        Select sel = new Select(element);
        sel.selectByVisibleText(text);
    }

    public static void selectBYValue(WebElement element, String value) {
        Select sel = new Select(element);
        sel.selectByValue(value);
    }

    public static void selectBYIndex(WebElement element, int index) {
        Select sel = new Select(element);
        sel.selectByIndex(index);
    }

    public static void scroll(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);

    }


    public static void takeScreenSHot(WebDriver driver) throws IOException {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File src = screenshot.getScreenshotAs(OutputType.FILE);
        File dest = new File("./screenshot.png");
        FileUtils.copyFile(src, dest);
    }


    public static String getValue(String key) {

        String path = System.getProperty("user.dir")
                + File.separator + "config.properties";
        String value = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            Properties prop = new Properties();
            prop.load(fis);
            value = prop.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    public void waitForElement(long sec) {
        try {
            Thread.sleep(sec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void assertTitle(WebElement element, String excepted) {
        String actual = element.getText();
        System.out.println("Actual Title is: " + actual);
        Assert.assertEquals(actual, excepted, "Title mismatch!");
    }

    public static void assertElementEnable(WebElement element){
        Assert.assertTrue(element.isEnabled(),"Element is not Enabled!");
    }

}
