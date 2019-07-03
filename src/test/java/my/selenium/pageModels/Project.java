package my.selenium.pageModels;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import my.selenium.utils.CommonUtils;
import my.selenium.utils.Params;

public class Project {
    protected final String ruumCanvas = "//ruum-canvas";
    protected final String canvasSections = ruumCanvas + "//ruum-canvas-section";
    protected final String addSectionButtons = ruumCanvas + "//add-canvas-section//button";
    protected final String addSectionOptions = Params.dropdownOptions;
    public final String canvasSyncState = "//ruum-project//canvas-sync-state";
    protected final String ruumExpandCollapseSectionsButton = "//expand-collapse-sections-button//button";
    protected final String parentGroupName = "//*[contains(@class, 'ruump-breadcrumb-group-name')]";
    protected RuumPopoverWindow popover = new RuumPopoverWindow();
    protected RuumModalWindow modalWindow = new RuumModalWindow();
    protected RuumNavBar navBar = new RuumNavBar();

    public void setRuumTitle(WebDriver driver, String title) throws Exception {
//    	Thread.sleep(2000);  // because of the delay. It happens, when project is created inside group
    	WebDriverWait wait = new WebDriverWait(driver, Params.timeOutInSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(navBar.ruumTitle)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Params.busyState)));  //  Now wait for invisibility of busy-wheel first
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(navBar.ruumTitle))).click();
//    	driver.findElement(By.xpath(navBar.ruumTitle)).clear();
    	driver.findElement(By.xpath(navBar.ruumTitle)).sendKeys(Keys.chord(Keys.CONTROL, "a"), title);
    }
    
    public String getRuumTitle(WebDriver driver) {
        return driver.findElement(By.xpath(navBar.ruumTitle)).getText();
    }

    public void toHaveRuumTitle(WebDriver driver, String expectedTitle) {
    	String realTitle = getRuumTitle(driver);
    	Assert.assertTrue("expected title = " + expectedTitle + "; real title = " + realTitle, CommonUtils.isElementExists(driver, navBar.ruumTitle + "[contains(text(),'" + expectedTitle + "')]"));
    }

    public RuumCanvasSection addBlankSection(WebDriver driver) {
    	driver.findElement(By.xpath(addSectionButtons + "[1]")).click();
    	driver.findElement(By.xpath(addSectionButtons + "//*[contains(text(),'Blank Section')]")).click();
        hasSavedChanges(driver);
        return getRuumSectionAtIndex(1);
    }

    public RuumCanvasSection getRuumSectionAtIndex(int index) {
        return new RuumCanvasSection(canvasSections + "[" + index + "]");
    }

    public RuumCanvasSection getRuumSectionByName(String name) {
        return new RuumCanvasSection(canvasSections + "//*[contains(text(),'" + name + "')]");
    }

    public void hasSavedChanges(WebDriver driver) {
    	Assert.assertTrue("'Saved' indicator is not shown", CommonUtils.isElementExists(driver, canvasSyncState + "//*[contains(text(),'Saved')]"));
    }
    
    public void invitePerson(WebDriver driver, String personEmail) {
    	driver.findElement(By.xpath(navBar.ruumInviteButton)).click();
    	driver.findElement(By.xpath(modalWindow.inputFields)).clear();   // only one input field exists now
    	driver.findElement(By.xpath(modalWindow.inputFields)).sendKeys(personEmail);
    	Actions action = new Actions(driver);
    	action.sendKeys(Keys.ENTER).build().perform();
    	modalWindow.pushButton(driver, "Invite");
        RuumSidePanel sidePanel = openSidePanel(driver,"Team");
		WebDriverWait wait = new WebDriverWait(driver, Params.timeOutInSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(sidePanel.teamProjNotVisitedParticipants + "//*[contains(text(),'" + personEmail + "')]")));
        Assert.assertTrue("New member " + personEmail + " is not invited", CommonUtils.isElementExists(driver, sidePanel.teamProjNotVisitedParticipants + "//*[contains(text(),'" + personEmail + "')]"));
    }

    public void removePerson(WebDriver driver, String personEmail) throws Exception {
    	RuumSidePanel sidePanel = openSidePanel(driver,"Team");
        sidePanel.removeParticipant(driver, personEmail);
    }

    public void checkUsersNumber(WebDriver driver, int expectedNumber) {
    	RuumSidePanel sidePanel = openSidePanel(driver,"Team");
    	Assert.assertEquals("Wrong number of users", expectedNumber, sidePanel.teamParticipantsCount(driver));
    }

    public void toHaveNeverAccessed(WebDriver driver, int expectedNumberOfNeverAccessed) {
    	RuumSidePanel sidePanel = openSidePanel(driver,"Team");
        int neverAccessed = sidePanel.getNeverAccessed(driver);
        Assert.assertEquals(neverAccessed + " is wrong number of Never Accessed", expectedNumberOfNeverAccessed, neverAccessed);
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
}
