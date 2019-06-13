package pageModels;

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

	public void enterTask(WebDriver driver, String description) {
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

//    async enterPoll(question, options) {
//        await t.pressKey('enter');
//        await t.click(this.sectionInsertButton);
//        await t.click(this.commonUtils.dropdownOptions.withText('Poll'));
//        await t.click(this.modalWindow.pollQuestionInputField);
//        await t.typeText(this.modalWindow.pollQuestionInputField, question);
//        let numOfOptions = await this.modalWindow.pollOptionsInputFields.count;
//        await t.expect(numOfOptions).eql(2);  //  2 - default number of options
//        //  add two options
//        await t.click(this.modalWindow.pollAddOptionButton);
//        await t.click(this.modalWindow.pollAddOptionButton);
//        numOfOptions = await this.modalWindow.pollOptionsInputFields.count;
//        await t.expect(numOfOptions).eql(4);  //  now must be 4 options
//        //  remove one option
//        await t.click(this.modalWindow.pollDeleteOptionIcons.nth(0));
//        numOfOptions = await this.modalWindow.pollOptionsInputFields.count;
//        await t.expect(numOfOptions).eql(3);  //  now must be 3 options
//        for(var i = 0; i < options.length; i++){
//            await t.typeText(this.modalWindow.pollOptionsInputFields.nth(i), options[i]);
//        }
//        await this.modalWindow.pushButton('Create Poll');
//    }
//
//    async enterTable() {
//        await t.pressKey('enter');
//        await t.click(this.sectionInsertButton);
//        await t.click(this.commonUtils.dropdownOptions.withText('Table'));
//        await t.wait(500);
//    }
//
//    async uploadFile(filePath, file) {
//        await t.pressKey('enter');
//        await t.click(this.sectionInsertButton);
//        await t.click(this.commonUtils.dropdownOptions.withText('File'));
//        await this.modalWindow.pushButton('My Device');
//        await t.setFilesToUpload(this.modalWindow.inputFields, filePath + '/' + file);  // only one input field exists now
//        await t.expect(this.modalWindow.attachedItems.withText(file).exists).ok('file is not added for uploading');
//        await this.modalWindow.pushButton('Add 1 File to Canvas');
//        await t.wait(1000);
//    }
//
//    async getSectionTaskByName(name) {
//        return new RuumSectionTask(this.taskPath.withText(name));
//    }
//
//    async getSectionPollByName(name) {
//        return new RuumSectionPoll(this.pollPath.withText(name));
//    }
//
//    async getSectionCustomStatusFieldByName(name) {
//        return this.customStatusPath.withText(name);
//    }
//
//    async toHaveText(expectedText) {
//        await t.expect(this.canvasSectionContent.withText(expectedText).exists).ok();
//    }
//
//    async toHaveHeader(expectedheader) {
//        await t.expect(this.canvasSectionName.withText(expectedheader).exists).ok();
//    }
// 
//    async toHaveTask(expectedText, expectedStatus) {
//        await t.expect(this.taskPath.withText(expectedText).exists).ok();
//        await t.expect(this.taskPath.find('task-status').find('.' + expectedStatus).exists).ok();
//    }
//    async clearSection() {
//        await t.click(this.canvasSectionContent.find('p'));
//        await t.click(this.canvasSectionContent.find('p'));
//        await t.click(this.canvasSectionContent.find('p'));
//        await t.pressKey('delete');
//    }
//
//    async deletePoll(pollName) {
//        var poll = await this.getSectionPollByName(pollName);
//        await t.click(poll.ruumPollSelector.find(this.ruumDropdownButton));
//        await t.click(this.commonUtils.dropdownOptions.withText('Delete'));
//        await this.modalWindow.pushButton('Cancel');
//        await poll.toExist();
//        await t.click(poll.ruumPollSelector.find(this.ruumDropdownButton));
//        await t.click(this.commonUtils.dropdownOptions.withText('Delete'));
//        await this.modalWindow.pushButton('Delete');
//        await poll.notToExist();
//    }
//
//    async removeAttachmentFromSection(fileName) {
//        await t.expect(this.attachmentsPaths.withText(fileName).exists).ok('Attachment ' + fileName + ' does not exist');
//        await t.click(this.attachmentsPaths.withText(fileName).find(this.ruumDropdownButton));
//        await t.click(this.commonUtils.dropdownOptions.withText('Remove from canvas'));
//        await t.wait(500);
//        await t.expect(this.attachmentsPaths.withText(fileName).exists).notOk();
//        // check the attachment still exists in side panel
//        const sidePanel = await this.project.openSidePanel('Files');
//        await t.expect(Selector(sidePanel.attachments).withText(fileName).exists).ok('file must exist in side panel');
//    }
//
//    async deleteAttachment(fileName) {
//        await t.expect(this.attachmentsPaths.withText(fileName).exists).ok('Attachment ' + fileName + ' does not exist');
//        await t.click(this.attachmentsPaths.withText(fileName).find(this.ruumDropdownButton));
//        await t.click(this.commonUtils.dropdownOptions.withText('Delete'));
//        await this.modalWindow.pushButton('Delete');
//        await t.wait(500);
//        await t.expect(this.attachmentsPaths.withText(fileName).exists).notOk();
//        // check the attachment was also deleted from side panel
//        const sidePanel = await this.project.openSidePanel('Files');
//        await t.expect(Selector(sidePanel.attachments).withText(fileName).exists).notOk('file must not exist in side panel');
//    }
}
