package my.selenium.pageModels;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import my.selenium.utils.CommonUtils;
import my.selenium.utils.Params;

public class RuumCanvasSection {
    protected String ruumSectionXPath;
    protected final String canvasSectionHeader = ruumSectionXPath + "//canvas-section-header";
    protected final String canvasSectionName = canvasSectionHeader + "//*[contains(@class, 'section-name')][1]";  //  PROBLEM!!! two <div> with same class name
    protected final String canvasSectionContent = ruumSectionXPath + "//canvas-section-content";
    protected final String taskPath = canvasSectionContent + "//ruum-task";
    protected final String customStatusPath = canvasSectionContent + "//custom-status";
    protected final String pollPath = canvasSectionContent + "//ruum-poll";
    protected final String tablePath = canvasSectionContent + "//table";
    protected final String processBarPath = canvasSectionContent + "//ruum-process-bar";
    protected final String processBarItems = processBarPath + "//*[contains(@class, 'process-bar-item')]";
    protected final String attachmentsPaths = canvasSectionContent + "//ruum-attachment";
    protected final String attachmentsPathImages = attachmentsPaths + "//ruum-attachment-image";
    protected final String attachmentsPathCards = attachmentsPaths + "//ruum-attachment-card";
    protected final String mailPath = canvasSectionContent + "//ruum-mail";
    protected final String mailSubject = mailPath + "//*[contains(@class, 'subject')]";
    protected final String mailText = mailPath + "//*[contains(@class, 'mail-text')]";
    protected final String sectionInsertButton = "//button//*[contains(text(),'Insert')]";
    protected final String ruumDropdownButton = "//*[contains(@class, 'ruum-dropdown')]//button";  //  TODO: make this path the same for all section's elements, whish have "..." button
    //  (see RuumSidePanel.ruumDropdownButton)
    protected Project project = new Project();
    protected RuumPopoverWindow popover = new RuumPopoverWindow();
    protected RuumModalWindow modalWindow = new RuumModalWindow();
    
	public RuumCanvasSection(){};
	
	public RuumCanvasSection(String sectionXPath){
		ruumSectionXPath = sectionXPath;
	}
	
	public void isExist(WebDriver driver){
		int size = driver.findElements(By.xpath(ruumSectionXPath)).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
	}

	public void enterHeader(WebDriver driver, String text) {
    	driver.findElement(By.xpath(canvasSectionName)).clear();
    	driver.findElement(By.xpath(canvasSectionName)).sendKeys(text);
    }

	public void enterText(WebDriver driver, String text) {
		driver.findElement(By.xpath(canvasSectionContent)).click();
        String[] arr = text.split("");
        int i;
        for (i = 0; i < arr.length; i++) {
        	driver.findElement(By.xpath(canvasSectionName)).sendKeys(arr[i]);
        }
    }

	public void enterTask(WebDriver driver, String description) throws Exception {
        if(CommonUtils.isElementNotExists(driver, sectionInsertButton)) {
        	Actions action = new Actions(driver);
        	action.sendKeys(Keys.ENTER).build().perform();
        }
        driver.findElement(By.xpath(sectionInsertButton)).click();
        driver.findElement(By.xpath(Params.dropdownOptions + "//*[contains(text(),'Task')]")).click();
        driver.findElement(By.xpath(taskPath + "[last()]" + "//task-description")).clear();
        driver.findElement(By.xpath(taskPath + "[last()]" + "//task-description")).sendKeys(description);

        //  workaround: these two pressKeys are required in order to save Task correctly
        Actions action = new Actions(driver);
    	action.sendKeys(Keys.ENTER).build().perform();
    	action.sendKeys(Keys.BACK_SPACE).build().perform();
    }

	public void enterCustomStatusField (WebDriver driver, String description, String customStatus) {
		Actions action = new Actions(driver);
    	action.sendKeys(Keys.ENTER).build().perform();
    	driver.findElement(By.xpath(sectionInsertButton)).click();
    	driver.findElement(By.xpath(Params.dropdownOptions + "//*[contains(text(),'Status Field')]")).click();
        driver.findElement(By.xpath(customStatusPath + "[last()]" + "//*[contains(@class,'description-input')]")).clear();
        driver.findElement(By.xpath(customStatusPath + "[last()]" + "//*[contains(@class,'description-input')]")).sendKeys(description);
        
        //  workaround: these two pressKeys are required in order to save Task correctly
        action.sendKeys(Keys.ENTER).build().perform();
    	action.sendKeys(Keys.BACK_SPACE).build().perform();
    	driver.findElement(By.xpath(customStatusPath + "[last()]" + "//*[contains(@class,'custom-status')]")).click();
    	driver.findElement(By.xpath("custom-status-popup//*[contains(@class,'custom-status')]" + customStatus)).click();       
    }

	public void enterPoll(WebDriver driver, String question, String[] options) {
		Actions action = new Actions(driver);
    	action.sendKeys(Keys.ENTER).build().perform();
    	driver.findElement(By.xpath(sectionInsertButton)).click();
    	driver.findElement(By.xpath(Params.dropdownOptions + "//*[contains(text(),'Poll')]")).click();
    	driver.findElement(By.xpath(modalWindow.pollQuestionInputField)).clear();   // only one input field exists now
    	driver.findElement(By.xpath(modalWindow.pollQuestionInputField)).sendKeys(question);
    	int numOfOptions = driver.findElements(By.xpath(modalWindow.pollOptionsInputFields)).size();
    	Assert.assertEquals(2, numOfOptions);  //  2 - default number of options
        //  add two options
    	driver.findElement(By.xpath(modalWindow.pollAddOptionButton)).click();
    	driver.findElement(By.xpath(modalWindow.pollAddOptionButton)).click();
    	numOfOptions = driver.findElements(By.xpath(modalWindow.pollOptionsInputFields)).size();
    	Assert.assertEquals(4, numOfOptions);  //  now must be 4 options
        //  remove one option
    	driver.findElement(By.xpath(modalWindow.pollDeleteOptionIcons + "/*[1]")).click();
    	numOfOptions = driver.findElements(By.xpath(modalWindow.pollOptionsInputFields)).size();
    	Assert.assertEquals(3, numOfOptions);  //  now must be 3 options
        for(int i = 0; i < options.length; i++){
        	driver.findElement(By.xpath(modalWindow.pollOptionsInputFields + "/*[" + (i+1) + "]")).sendKeys(options[i]);
        }
        modalWindow.pushButton(driver, "Create Poll");
    }

	public void enterTable(WebDriver driver) throws Exception {
		Actions action = new Actions(driver);
    	action.sendKeys(Keys.ENTER).build().perform();
		driver.findElement(By.xpath(sectionInsertButton)).click();
    	driver.findElement(By.xpath(Params.dropdownOptions + "//*[contains(text(),'Table')]")).click();
    	Thread.sleep(500);
    }

    public void uploadFile(WebDriver driver, String filePath, String file) throws Exception {
    	driver.findElement(By.xpath(modalWindow.inputFields)).sendKeys(filePath + '/' + file);  // only one input field exists now
    	Assert.assertTrue("file is not added for uploading", CommonUtils.isElementExists(driver, modalWindow.attachedItems + "//*[contains(text(),'" + file + "')]"));
    	modalWindow.pushButton(driver, "Add 1 File to Canvas");
    	Thread.sleep(1000);
    }

    public RuumSectionTask getSectionTaskByName(WebDriver driver, String name) {
        return new RuumSectionTask(taskPath + "//*[contains(text(),'" + name + "')]");
    }

    public RuumSectionPoll getSectionPollByName(WebDriver driver, String name) {
        return new RuumSectionPoll(pollPath + "//*[contains(text(),'" + name + "')]");
    }

    public RuumSectionPoll getSectionCustomStatusFieldByName(WebDriver driver, String name) {
    	return new RuumSectionPoll(customStatusPath + "//*[contains(text(),'" + name + "')]");
    }

    public void toHaveText(WebDriver driver, String expectedText) {
    	Assert.assertTrue(CommonUtils.isElementExists(driver, canvasSectionContent + "//*[contains(text(),'" + expectedText + "')]"));
    }

    public void toHaveHeader(WebDriver driver, String expectedheader) {
    	Assert.assertTrue(CommonUtils.isElementExists(driver, canvasSectionName + "//*[contains(text(),'" + expectedheader + "')]"));
    }
 
    public void toHaveTask(WebDriver driver, String expectedText, String expectedStatus) {
    	Assert.assertTrue(CommonUtils.isElementExists(driver, taskPath + "//*[contains(text(),'" + expectedText + "')]"));
        Assert.assertTrue(CommonUtils.isElementExists(driver, taskPath + "//task-status" + "//*[contains(@class, 'expectedStatus')]"));
    }
    
    public void clearSection(WebDriver driver) {
    	driver.findElement(By.xpath(canvasSectionContent + "//p")).click();
    	driver.findElement(By.xpath(canvasSectionContent + "//p")).click();
    	driver.findElement(By.xpath(canvasSectionContent + "//p")).click();
    	Actions action = new Actions(driver);
    	action.sendKeys(Keys.DELETE).build().perform();
    }

    public void deletePoll(WebDriver driver, String pollName) {
        RuumSectionPoll poll = getSectionPollByName(driver, pollName);
		driver.findElement(By.xpath(poll.ruumPollXPath + "//" + ruumDropdownButton)).click();
    	driver.findElement(By.xpath(Params.dropdownOptions + "//*[contains(text(),'Delete')]")).click();
    	modalWindow.pushButton(driver, "Cancel");
        poll.isExist(driver);
		driver.findElement(By.xpath(poll.ruumPollXPath + "//" + ruumDropdownButton)).click();
    	driver.findElement(By.xpath(Params.dropdownOptions + "//*[contains(text(),'Delete')]")).click();
    	modalWindow.pushButton(driver, "Delete");
        poll.isNotExist(driver);
    }

    public void removeAttachmentFromSection(WebDriver driver, String fileName) throws Exception {
    	Assert.assertTrue("Attachment " + fileName + " does not exist", CommonUtils.isElementExists(driver, attachmentsPaths + "//*[contains(text(),'" + fileName + "')]"));
		driver.findElement(By.xpath(attachmentsPaths + "//*[contains(text(),'" + fileName + "')]")).click();
    	driver.findElement(By.xpath(Params.dropdownOptions + "//*[contains(text(),'Remove from canvas')]")).click();
        Thread.sleep(500);
        Assert.assertTrue( CommonUtils.isElementNotExists(driver, attachmentsPaths + "//*[contains(text(),'" + fileName + "')]"));
        // check the attachment still exists in side panel
        RuumSidePanel sidePanel = project.openSidePanel(driver, "Files");
        Assert.assertTrue("file must exist in side panel", CommonUtils.isElementExists(driver, sidePanel.attachments + "//*[contains(text(),'" + fileName + "')]"));
    }

    public void deleteAttachment(WebDriver driver, String fileName) throws Exception {
    	Assert.assertTrue("Attachment " + fileName + " does not exist", CommonUtils.isElementExists(driver, attachmentsPaths + "//*[contains(text(),'" + fileName + "')]"));
		driver.findElement(By.xpath(attachmentsPaths + "//*[contains(text(),'" + fileName + "')]" + "//" + ruumDropdownButton)).click();
    	driver.findElement(By.xpath(Params.dropdownOptions + "//*[contains(text(),'Delete')]")).click();
    	modalWindow.pushButton(driver, "Delete");
    	Thread.sleep(500);
    	Assert.assertTrue( CommonUtils.isElementNotExists(driver, attachmentsPaths + "//*[contains(text(),'" + fileName + "')]"));
        // check the attachment was also deleted from side panel
    	RuumSidePanel sidePanel = project.openSidePanel(driver, "Files");
    	Assert.assertTrue("file must not exist in side panel", CommonUtils.isElementNotExists(driver, sidePanel.attachments + "//*[contains(text(),'" + fileName + "')]"));
    }
}
