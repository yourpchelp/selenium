package pageModels;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import my.selenium.utils.CommonUtils;

public class RuumSidePanel {
	protected final String ruumSidePanelXPath = "//ruum-sidepanel-container";
	protected final String teamGroupParticipants = ruumSidePanelXPath + "//ruum-project-group-team-list-item";
	protected final String teamProjActiveParticipants = ruumSidePanelXPath + "ruum-project-team-active-participants//ruum-team-list-item";
	protected final String ruumSidepanelTitle = "//ruum-sidepanel-title";
	protected final String teamProjNotVisitedParticipants = ruumSidePanelXPath + "//ruum-project-team-never-visited-participants//ruum-team-list-item";
	protected final String ruumDropdownButton = "//ruum-dropdown-desktop//*[data-test='ruum-dropdown-button']";
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
    	return driver.findElements(By.xpath(teamProjActiveParticipants)).size() + 
    	driver.findElements(By.xpath(teamProjNotVisitedParticipants)).size() + 
    	driver.findElements(By.xpath(teamGroupParticipants)).size();
    }
    
    public int getNeverAccessed(WebDriver driver) {
        if(CommonUtils.isElementExists(driver, teamProjNotVisitedParticipants)) {
            return driver.findElements(By.xpath(teamProjNotVisitedParticipants)).size();
        }
        return 0;
    }
    
    public void removeParticipant(WebDriver driver, String personEmail) {
        String activeParticipant = teamProjActiveParticipants + "//*[contains(text(),'" + personEmail + "')]";
        String notVisitedParticipant = teamProjNotVisitedParticipants + "//*[contains(text(),'" + personEmail + "')]";
        String groupParticipant = teamGroupParticipants + "//*[contains(text(),'" + personEmail + "')]";
        if(CommonUtils.isElementExists(driver, activeParticipant)) {
        	driver.findElement(By.xpath(activeParticipant + ruumDropdownButton)).click();
        }
        else if(CommonUtils.isElementExists(driver, notVisitedParticipant)) {
        	driver.findElement(By.xpath(notVisitedParticipant + ruumDropdownButton)).click();
        }
        else if(CommonUtils.isElementExists(driver, groupParticipant)) {
        	driver.findElement(By.xpath(groupParticipant + ruumDropdownButton)).click();
        }
        else {
        	Assert.fail("Ruum member " + personEmail + " does not exixt");
        }
        driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Remove from')]")).click();  //  "Remove from Project" or "Remove from Group"
        modalWindow.pushButton(driver, "Remove");
        Assert.assertTrue("Ruum member " + personEmail + " is not removed", CommonUtils.isElementExists(driver, activeParticipant) || 
        																	CommonUtils.isElementExists(driver, notVisitedParticipant) || 
        																	CommonUtils.isElementExists(driver, groupParticipant));
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
