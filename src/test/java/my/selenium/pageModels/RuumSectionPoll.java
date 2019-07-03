package my.selenium.pageModels;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import my.selenium.utils.CommonUtils;

public class RuumSectionPoll {
	protected String ruumPollXPath;
	protected final String pollQuestion = ruumPollXPath + "//*[contains(@class, 'question')]";
	protected final String pollOptionLines = ruumPollXPath + "//*[contains(@class, 'option-line')]";
	protected final String pollOptionDescriptions = pollOptionLines + "//*[contains(@class, 'desc')]";
    protected RuumPopoverWindow popover = new RuumPopoverWindow();

	public RuumSectionPoll(){};
	
	public RuumSectionPoll(String pollXPath){
		ruumPollXPath = pollXPath;
	}

	public void select(WebDriver driver) {
		driver.findElement(By.xpath(ruumPollXPath)).click();
    }
	
	public void isExist(WebDriver driver){
		int size = driver.findElements(By.xpath(ruumPollXPath)).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
	}
	
	public void isNotExist(WebDriver driver){
		int size = driver.findElements(By.xpath(ruumPollXPath)).size();
		Assert.assertTrue(String.valueOf(size), size == 0);
	}

	public void toHaveQuestion(WebDriver driver, String expectedText) {
    	int size = driver.findElements(By.xpath(pollQuestion + "//*[contains(text(),'" + expectedText + "')]")).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
    }

	public void chooseOption(WebDriver driver, String optionDescription) {
        // choose the option if it is not chosen
		boolean isChosen = CommonUtils.isElementExists(driver, optionDescription + "/parent::*" + "//*[contains(@class, 'icon-task-completed')]");
        if(!isChosen) {
        	driver.findElement(By.xpath(pollOptionDescriptions + "//*[contains(text(),'" + optionDescription + "')]")).click();
        }
    }

	public void toHaveOptionChosen(WebDriver driver, String optionDescription, boolean mustBeChosen) {
		boolean isChosen = CommonUtils.areElementsExist(driver, optionDescription + "/parent::*" + "//*[contains(@class, 'icon-task-completed')]");
        if(mustBeChosen) {
        	Assert.assertTrue(isChosen);
        }
        else {
        	Assert.assertFalse(isChosen);
        }
                
    }

	public void checkCountBarWidth(WebDriver driver, String optionDescription, String expectedWidth){
		String styleAttribute = driver.findElement(By.xpath(pollOptionDescriptions + "//*[contains(text(),'" + optionDescription + "')]" + 
				"/parent::*" + "/following-sibling::[contains(@class, 'vote-count')]" + "//*[contains(@class, 'count-bar')]")).getAttribute("style");
		Pattern pattern = Pattern.compile("(?<=\\s).*(?=\\;)");
		Matcher matcher = pattern.matcher(styleAttribute);
		if (!matcher.find()){
			Assert.fail("inputsequence doesn't match this matcher's pattern");
		}
		Assert.assertEquals(matcher.group(1) + " is wrong width of CountBar", expectedWidth, matcher.group(1));
    }

    public void toHavePersonChoseOption(WebDriver driver, String optionDescription, String personName, String personAvatar) {
    	Assert.assertTrue("User " + personAvatar + " is not assigned to the task", 
    			CommonUtils.isElementExists(driver, pollOptionDescriptions + "//*[contains(text(),'" + optionDescription + "')]" + 
    					"/parent::*" + "/following-sibling::[contains(@class, 'vote-count')]" + "//task-assignees" + "//ruum-avatar//button" + 
    					"//*[contains(text(),'" + personAvatar + "')]"));
    }
}
