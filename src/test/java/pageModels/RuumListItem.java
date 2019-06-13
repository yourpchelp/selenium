package pageModels;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import my.selenium.utils.CommonUtils;

public class RuumListItem {
	protected String ruumListItemXPath;
	protected final String ruumListItemName = ruumListItemXPath + "//*[@data-test='ruum-lobby-list-item-name']";
	protected final String ruumListItemStatus = ruumListItemXPath + "//ruum-status";
	protected final String ruumListItemFavorite = ruumListItemXPath + "//*[@data-test='ruum-lobby-list-item-fav']//button";
	protected final String ruumListItemLabels = ruumListItemXPath + "//*[@data-test='ruum-lobby-list-item-labels']";
	protected final String ruumListItemOptions = ruumListItemXPath + "//*[@data-test='ruum-lobby-list-item-more-options']//button[2]";
	protected final String ruumListItemCreator = ruumListItemXPath + "//*[@data-test='ruum-lobby-list-item-creator']";
	protected RuumPopoverWindow popover = new RuumPopoverWindow();
	protected RuumModalWindow modalWindow = new RuumModalWindow();
    protected Project project = new Project();
	
	public RuumListItem(){};
	
	public RuumListItem(String listItemXPath){
		ruumListItemXPath = listItemXPath;
	}
	
	public void isExist(WebDriver driver){
		int size = driver.findElements(By.xpath(ruumListItemXPath)).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
	}
	
	public void isNotExist(WebDriver driver){
		int size = driver.findElements(By.xpath(ruumListItemXPath)).size();
		Assert.assertTrue(String.valueOf(size), size == 0);
	}
	
	public void select(WebDriver driver) {
		driver.findElement(By.xpath(ruumListItemName)).click();
    }

    public String getRuumCreator(WebDriver driver) {
    	return driver.findElement(By.xpath(ruumListItemCreator)).getText();
    }

    public void checkRuumCreator(WebDriver driver, String expectedCreator) {
    	Assert.assertEquals("Wrong ruum owner", expectedCreator, getRuumCreator(driver));
    }

    public void setStatus(WebDriver driver, String status) throws Exception {
    	driver.findElement(By.xpath(ruumListItemStatus)).click();
    	driver.findElement(By.xpath(popover.ruumPopoverStatuses + "//*[contains(text(),'" + status + "')]")).click();

        if(status.equals("Completed")) {
        	Thread.sleep(2000);
        	Actions action = new Actions(driver);
        	action.sendKeys(Keys.ESCAPE).build().perform();  //  to close feedback popup
        }
    }

    public void toHaveStatus(WebDriver driver, String expectedStatus) {
    	if(expectedStatus.equals("No Status")) {
            expectedStatus = "Set Status";
        }
    	int size = driver.findElements(By.xpath(ruumListItemStatus + "//*[contains(text(),'" + expectedStatus + "')]")).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
    }

    public void toHaveFavorite(WebDriver driver) {
    	Assert.assertTrue("Ruum is not favorited", driver.findElement(By.xpath(ruumListItemFavorite)).getAttribute("class").contains("btn-link-primary"));
    }

    public void toHaveUnfavorite(WebDriver driver) {
    	Assert.assertFalse("Ruum is still favorited", driver.findElement(By.xpath(ruumListItemFavorite)).getAttribute("class").contains("btn-link-primary"));
    }

    public void addLabel(WebDriver driver, String label) {
    	driver.findElement(By.xpath(ruumListItemLabels)).click();
    	driver.findElement(By.xpath(popover.ruumPopoverInput)).clear();
    	driver.findElement(By.xpath(popover.ruumPopoverInput)).sendKeys(label);
        popover.pushButton(driver, "Create");
    }

    public void toHaveLabel(WebDriver driver, String expectedLabel) {
		int size = driver.findElements(By.xpath(ruumListItemLabels + "//*[contains(text(),'" + expectedLabel + "')]")).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
    }

    public void toNotHaveLabel(WebDriver driver, String expectedLabel) {
    	int size = driver.findElements(By.xpath(ruumListItemLabels + "//*[contains(text(),'" + expectedLabel + "')]")).size();
		Assert.assertTrue(String.valueOf(size), size == 0);
    }

    public void addExistingLabel(WebDriver driver, String label) {
    	driver.findElement(By.xpath(ruumListItemLabels)).click();
    	driver.findElement(By.xpath(popover.ruumPopoverRuumTags + "//*[contains(text(),'" + label + "')]")).click();
    	Actions action = new Actions(driver);
    	action.sendKeys(Keys.ESCAPE).build().perform();  //  to close a popup
    }

    public void deleteLabel(WebDriver driver, String label) {
    	driver.findElement(By.xpath(ruumListItemLabels)).click();
    	boolean ifExists = CommonUtils.isElementExists(driver, popover.ruumPopoverRuumTags + "//*[contains(text(),'" + label + "')]" + "//*[contains(@class, 'icon-cancel')]");
        if(ifExists) {
            driver.findElement(By.xpath(popover.ruumPopoverRuumTags + "//*[contains(text(),'" + label + "')]")).click();
            Actions action = new Actions(driver);
        	action.sendKeys(Keys.ESCAPE).build().perform();  //  to close a popup
        }
    }

    public void moveProjectToGroup(WebDriver driver, String groupName) {
    	driver.findElement(By.xpath(ruumListItemOptions)).click();
    	driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Move Project to Group…')]")).click();
    	driver.findElement(By.xpath(modalWindow.groupsNames + "//*[contains(text(),'" + groupName + "')]")).click();
        modalWindow.pushButton(driver, "Move");
    }

    public void removeProjectFromGroup(WebDriver driver) {
    	driver.findElement(By.xpath(ruumListItemOptions)).click();
    	driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Remove Project from Group')]")).click();
        modalWindow.pushButton(driver, "Remove");
    }

    public void duplicateProject(WebDriver driver, String newProjectName) {
    	driver.findElement(By.xpath(ruumListItemOptions)).click();
    	driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Duplicate Project')]")).click();
    	driver.findElement(By.xpath(modalWindow.inputFields)).click();  // only one input field exists now
    	driver.findElement(By.xpath(modalWindow.inputFields)).clear();
    	driver.findElement(By.xpath(modalWindow.inputFields)).sendKeys(newProjectName);
        modalWindow.pushButton(driver, "Duplicate");
    }

    public void createTemplateFromProject(WebDriver driver, String templateName) throws Exception {
    	driver.findElement(By.xpath(ruumListItemOptions)).click();
    	driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Create Template from Project')]")).click();
        project.setRuumTitle(driver, templateName);
        //  the following click is used for exiting from title field
        Actions action = new Actions(driver);
    	action.sendKeys(Keys.ENTER).build().perform();
    	Thread.sleep(1000);
    }
}
