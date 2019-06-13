package my.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

public class CommonUtils {
    public static void resizeBrowser(WebDriver driver, int width, int height) {
    	Dimension d = new Dimension(width, height);
    	//Resize current window to the set dimension
        driver.manage().window().setSize(d);
    }
    
	public static boolean areElementsExist(WebDriver driver, String xPath){
		int size = driver.findElements(By.xpath(xPath)).size();
		return (size > 0);
	}
	
	public static boolean isElementExists(WebDriver driver, String xPath){
		int size = driver.findElements(By.xpath(xPath)).size();
		return (size == 1);
	}
	
	public static boolean isElementNotExists(WebDriver driver, String xPath){
		int size = driver.findElements(By.xpath(xPath)).size();
		return (size == 0);
	}
}
