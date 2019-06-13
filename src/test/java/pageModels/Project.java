package pageModels;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import my.selenium.utils.CommonUtils;
import my.selenium.utils.Params;

public class Project {
    protected final String ruumCanvas = "//ruum-canvas";
    protected final String canvasSections = ruumCanvas + "//ruum-canvas-section";
    protected final String addSectionButtons = ruumCanvas + "//add-canvas-section//button";
    protected final String addSectionOptions = Params.dropdownOptions;
    protected final String canvasSyncState = "//ruum-project//canvas-sync-state";
    protected final String ruumTitle = "//*[contains(@class, 'ruum-breadcrumb-active-item')]";  //  TODO: At first, this selector points on groupName, and only after the delay is begins to point on projectName ("New Project")
    protected final String ruumInviteButton = "//button[contains(text(),'Invite')]";
    protected final String ruumExpandCollapseSectionsButton = "//expand-collapse-sections-button//button";
    protected final String parentGroupName = "//*[contains(@class, 'ruump-breadcrumb-group-name')]";
    protected RuumPopoverWindow popover = new RuumPopoverWindow();
    protected RuumModalWindow modalWindow = new RuumModalWindow();

    public void setRuumTitle(WebDriver driver, String title) throws Exception {
    	Thread.sleep(2000);  // because of the delay (see above TODO). It happens, when project is created inside group
    	driver.findElement(By.xpath(ruumTitle)).clear();
    	driver.findElement(By.xpath(ruumTitle)).sendKeys(title);
    }
    
    public String getRuumTitle(WebDriver driver) {
        return driver.findElement(By.xpath(ruumTitle)).getText();
    }

    public void toHaveRuumTitle(WebDriver driver, String expectedTitle) {
    	String realTitle = getRuumTitle(driver);
    	Assert.assertTrue("expected title = " + expectedTitle + "; real title = " + realTitle, CommonUtils.isElementExists(driver, ruumTitle + "//*[contains(text(),'" + expectedTitle + "')]"));
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
    	driver.findElement(By.xpath(ruumInviteButton)).click();
    	driver.findElement(By.xpath(modalWindow.inputFields)).clear();   // only one input field exists now
    	driver.findElement(By.xpath(modalWindow.inputFields)).sendKeys(personEmail);
    	Actions action = new Actions(driver);
    	action.sendKeys(Keys.ENTER).build().perform();
    	modalWindow.pushButton(driver, "Invite");
        RuumSidePanel sidePanel = openSidePanel(driver,"Team");
        Assert.assertTrue("New member " + personEmail + " is not invited", CommonUtils.isElementExists(driver, sidePanel.teamProjNotVisitedParticipants + "//*[contains(text(),'" + personEmail + "')]"));
    }

    public void removePerson(WebDriver driver, String personEmail) {
    	RuumSidePanel sidePanel = openSidePanel(driver,"Team");
        sidePanel.removeParticipant(driver, personEmail);
    }

    public void checkUsersNumber(WebDriver driver, String expectedNumber) {
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
        boolean isActive = driver.findElement(By.xpath("//button[@title='" + contentOfPanel + "']")).getAttribute("class").contains("btn-primary-light");
        if (!isActive) {
        	driver.findElement(By.xpath("//button[@title='" + contentOfPanel + "']")).click();
        }
        return new RuumSidePanel();
    }
}
