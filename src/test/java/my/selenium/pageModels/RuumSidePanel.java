package my.selenium.pageModels;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import my.selenium.utils.CommonUtils;
import my.selenium.utils.Params;

public class RuumSidePanel {
	protected final String ruumSidePanelXPath = "//ruum-sidepanel-container";
	protected final String teamGroupParticipants = ruumSidePanelXPath + "//ruum-project-group-team-list-item";
	protected final String teamProjActiveParticipants = ruumSidePanelXPath + "//ruum-project-team-active-participants//ruum-team-list-item";
	protected final String ruumSidepanelTitle = "//ruum-sidepanel-title";
	protected final String teamProjNotVisitedParticipants = ruumSidePanelXPath + "//ruum-project-team-never-visited-participants//ruum-team-list-item";
	protected final String ruumDropdownButton = "//ruum-dropdown-desktop//*[@data-test='ruum-dropdown-button']";
	protected final String teamInviteButton = ruumSidePanelXPath + "//ruum-participants-container//button//*[contains(text(),'Invite')]";
	protected final String tasksPanelMenu = ruumSidePanelXPath + "//ruum-tasks-container//*[contains(@class,'header')]";
	protected final String tasksGroups = ruumSidePanelXPath + "//ruum-tasks-container//*[contains(@class,'task-groups')]";
	protected final String attachmentsGroups = ruumSidePanelXPath + "//ruum-project-attachment-list";
	protected final String attachments = attachmentsGroups + "//ruum-project-attachment-item";
	protected final String attachmentUploadInput = ruumSidePanelXPath + "//ruum-project-attachment-upload//input";
    protected RuumPopoverWindow popover = new RuumPopoverWindow();
    protected RuumModalWindow modalWindow = new RuumModalWindow();
    
    public int teamParticipantsCount(WebDriver driver){
        //  this sum is acceptable, because we always have either group's or project's participants (separately, not together)
//		WebDriverWait wait = new WebDriverWait(driver, Params.timeOutInSeconds);
//		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Params.busyState)));  //  Now wait for invisibility of busy-wheel first 
//		wait.until(ExpectedConditions.elementToBeClickable(sidePanelButton)).click();
    	int sum = driver.findElements(By.xpath(teamProjActiveParticipants)).size() + 
    	driver.findElements(By.xpath(teamProjNotVisitedParticipants)).size() + 
    	driver.findElements(By.xpath(teamGroupParticipants)).size();
    	return sum;
    }
    
    public int getNeverAccessed(WebDriver driver) {
        return driver.findElements(By.xpath(teamProjNotVisitedParticipants)).size();
    }
    
    public void removeParticipant(WebDriver driver, String personEmail) throws Exception {
        String activeParticipant = teamProjActiveParticipants + "[contains(.,'" + personEmail + "')]";
        String notVisitedParticipant = teamProjNotVisitedParticipants + "[contains(.,'" + personEmail + "')]";
        String groupParticipant = teamGroupParticipants + "[contains(.,'" + personEmail + "')]";
        String[] arr = new String[] {activeParticipant, notVisitedParticipant, groupParticipant};
        boolean flag = false;
        for (String str : arr){
        	boolean isElementPresent = CommonUtils.isElementExists(driver, str);
            if(isElementPresent) {
            	flag = true;
        		WebDriverWait wait = new WebDriverWait(driver, Params.timeOutInSeconds); 
        		WebElement el = driver.findElement(By.xpath(str + ruumDropdownButton));
        		wait.until(ExpectedConditions.elementToBeClickable(el));
        		el.sendKeys(Keys.ENTER);
                popover.pushButton(driver, "Remove from");  //  "Remove from Project" or "Remove from Group"
                modalWindow.pushButton(driver, "Remove");
                for (String elem : arr){
                	Assert.assertTrue("Ruum member " + personEmail + " is not removed", CommonUtils.isElementNotExists(driver, elem));
                }
                break;
            }
        }
        Assert.assertTrue("Ruum member " + personEmail + " does not exist", flag);
    }
    
    public void uploadFile(WebDriver driver, String filePath, String file) throws Exception {
    	driver.findElement(By.xpath(attachmentUploadInput)).sendKeys(filePath + '/' + file);
    	Assert.assertTrue("file is not added for uploading", CommonUtils.isElementExists(driver, attachments + "//*[contains(text(),'" + file + "')]"));
    	Thread.sleep(1000);
    }

    public int getAttachmentsCount(WebDriver driver){
        return driver.findElements(By.xpath(attachments)).size();
    }

    public int getNumberAttachmentsByGroup(WebDriver driver, String group) {
        //  group must be Spreadsheets, Documents, Presentations, Images or Other
        if(CommonUtils.isElementExists(driver, attachments)) {
            String text = driver.findElement(By.xpath(attachmentsGroups + "//*[contains(text(),'" + group + "')]")).getText();
            text = text.replaceAll("\r\n|\n|\r","");  //  exclude \n from the string (to conjunct it into one line)
    		Pattern pattern = Pattern.compile(group + ".*\\)");
    		Matcher matcher = pattern.matcher(text);
            String res = matcher.group(1);
            pattern = Pattern.compile("(?<=\\s).*(?=\\;)");
    		matcher = pattern.matcher(res);
            return Integer.valueOf(matcher.group(1));
        }
        return 0;
    }

    public void addAttachmentToSection(WebDriver driver, RuumCanvasSection section, String fileName) throws Exception {
    	driver.findElement(By.xpath(section.canvasSectionContent)).click();  // click on Section to make it focused
    	Assert.assertTrue("Attachment " + fileName + " does not exist", CommonUtils.isElementExists(driver, attachments + "//*[contains(text(),'" + fileName + "')]"));
    	driver.findElement(By.xpath(attachments + "//*[contains(text(),'" + fileName + "')]" + "//" + ruumDropdownButton)).click();
    	driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Add to canvas')]")).click();
    	Thread.sleep(500);
    	Assert.assertTrue(CommonUtils.isElementExists(driver, section.attachmentsPaths + "//*[contains(text(),'" + fileName + "')]"));
    }

    public void removeAttachmentFromSection(WebDriver driver, RuumCanvasSection section, String fileName) throws Exception {
    	Assert.assertTrue("Attachment " + fileName + " does not exist", CommonUtils.isElementExists(driver, attachments + "//*[contains(text(),'" + fileName + "')]"));
    	driver.findElement(By.xpath(attachments + "//*[contains(text(),'" + fileName + "')]" + "//" + ruumDropdownButton)).click();
    	driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Remove from canvas')]")).click();
    	Thread.sleep(500);
    	Assert.assertFalse(CommonUtils.isElementExists(driver, section.attachmentsPaths + "//*[contains(text(),'" + fileName + "')]"));
        // check the attachment stil exists in side panel
    	Assert.assertTrue("File " + fileName + " must exist in side panel", CommonUtils.isElementExists(driver, attachments + "//*[contains(text(),'" + fileName + "')]"));
    }

    public void deleteAttachment(WebDriver driver, RuumCanvasSection section, String fileName) throws Exception {
    	Assert.assertTrue("Attachment " + fileName + " does not exist", CommonUtils.isElementExists(driver, attachments + "//*[contains(text(),'" + fileName + "')]"));
    	driver.findElement(By.xpath(attachments + "//*[contains(text(),'" + fileName + "')]" + "//" + ruumDropdownButton)).click();
    	driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Delete')]")).click();
    	modalWindow.pushButton(driver, "Delete");
    	Thread.sleep(500);
    	Assert.assertFalse(CommonUtils.isElementExists(driver, section.attachmentsPaths + "//*[contains(text(),'" + fileName + "')]"));
        // check the attachment was also deleted from side panel
    	Assert.assertTrue("File " + fileName + " must not exist in side panel", CommonUtils.isElementExists(driver, attachments + "//*[contains(text(),'" + fileName + "')]"));
    }
}
