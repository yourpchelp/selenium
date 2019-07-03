package my.selenium.utils;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import my.selenium.pageModels.RuumPopoverWindow;


public class CommonUtils {
	private static String _uniqueTestIdentifier = null;
	
    public static String uniqueTestIdentifier(){
        if (_uniqueTestIdentifier != null && !_uniqueTestIdentifier.isEmpty()) {
            return _uniqueTestIdentifier;
        } 

        Date now = new Date();
        int mm = now.getMonth()+1; // getMonth() is zero-based
        int dd = now.getDate();
        int HH = now.getHours();
        int MM = now.getMinutes();
        String dateString = String.valueOf(100*mm + dd + HH + MM);
        _uniqueTestIdentifier = dateString + "_" + Double.toString(Math.random()).substring(2, 7);
//        _uniqueTestIdentifier = tmpStr.substring(2, 7);
        return _uniqueTestIdentifier;
    }
    
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
	
	public static boolean isElementNotExists(WebDriver driver, String xPath) throws Exception{
//		Thread.sleep(5000);  //  5 seconds is enough to wait for elements appearance (if they exist)
		int size = driver.findElements(By.xpath(xPath)).size();
		return (size == 0);
	}

	public static String takeChosenDate(WebDriver driver) {
        RuumPopoverWindow popover = new RuumPopoverWindow();
        String day = driver.findElement(By.xpath(popover.datePickerSelectedDay)).getText();
        String monthYear = driver.findElement(By.xpath(popover.datePickerNavigation)).getText();
        monthYear = monthYear.replaceAll("(\r\n|\n|\r)","");  // to exclude \n \r (for chrome)
        String shortenMonth = monthYear.substring(0, 3);
        return day + " " + shortenMonth;
	}
}
