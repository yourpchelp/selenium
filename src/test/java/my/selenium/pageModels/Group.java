package my.selenium.pageModels;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import my.selenium.utils.Params;

public class Group {
	protected RuumPopoverWindow popover = new RuumPopoverWindow();
	protected RuumList ruumList = new RuumList();
	protected RuumModalWindow modalWindow = new RuumModalWindow();
	protected RuumNavBar navBar = new RuumNavBar();
    protected final String dropDownButton = "(//ruum-project-group-options//button)[2]";
    protected final String inviteButton = "//button//*[contains(text(),'Invite')]";

    public void renameGroupFromInsideIt(WebDriver driver, String newTitle) {
    	driver.findElement(By.xpath(dropDownButton)).click();
    	driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Rename Group')]")).click();
    	driver.findElement(By.xpath(modalWindow.inputFields + "[1]")).clear(); // use first input field
    	driver.findElement(By.xpath(modalWindow.inputFields + "[1]")).sendKeys(newTitle);
    	modalWindow.pushButton(driver, "Update");
    	WebDriverWait wait = new WebDriverWait(driver, Params.timeOutInSeconds);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Params.busyState)));  //  Now wait for invisibility of busy-wheel first
    	driver.findElement(By.xpath(navBar.ruumLogo)).click();
    }

    public void invitePerson(WebDriver driver, String personEmail) {
    	driver.findElement(By.xpath(navBar.ruumInviteButton)).click();
    	driver.findElement(By.xpath(modalWindow.inputFields)).clear();   // only one input field exists now
    	driver.findElement(By.xpath(modalWindow.inputFields)).sendKeys(personEmail);
    	Actions action = new Actions(driver);
    	action.sendKeys(Keys.ENTER).build().perform();
    	modalWindow.pushButton(driver, "Invite");
        RuumSidePanel sidePanel = openSidePanel(driver,"Team");
        try{
        	WebDriverWait wait = new WebDriverWait(driver, Params.timeOutInSeconds);
        	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(sidePanel.teamGroupParticipants + "//*[contains(text(),'" + personEmail + "')]")));
        }
        catch(Exception e){
            Assert.fail("New member " + personEmail + " is not invited\n" + e);
        }


    }
    
    public RuumSidePanel openSidePanel(WebDriver driver, String contentOfPanel) {
        // open the side panel if it is closed
    	// contentOfPanel can be Team, Tasks, Files, Activities, Emails, Variables or Info
    	WebElement sidePanelButton = navBar.getSidePanelButton(driver, contentOfPanel);
        boolean isActive = sidePanelButton.getAttribute("class").contains("btn-primary-light");
        if (!isActive) {
    		WebDriverWait wait = new WebDriverWait(driver, Params.timeOutInSeconds);
   			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Params.busyState)));  //  Now wait for invisibility of busy-wheel first 
    		wait.until(ExpectedConditions.elementToBeClickable(sidePanelButton)).click();
        }
        return new RuumSidePanel();
    }
    
    public void checkUsersNumber(WebDriver driver, int expectedNumber) {
    	RuumSidePanel sidePanel = openSidePanel(driver,"Team");
    	Assert.assertEquals("Wrong number of users", expectedNumber, sidePanel.teamParticipantsCount(driver));
    }

    public void removePerson(WebDriver driver, String personEmail) throws Exception {
    	RuumSidePanel sidePanel = openSidePanel(driver,"Team");
        sidePanel.removeParticipant(driver, personEmail);
    }
}
