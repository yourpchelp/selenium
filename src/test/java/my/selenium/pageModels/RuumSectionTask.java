package my.selenium.pageModels;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import my.selenium.utils.CommonUtils;
import my.selenium.utils.Params;

public class RuumSectionTask {
	protected String ruumTaskXPath;
	protected final String taskStatusButton = ruumTaskXPath + "//task-status";
	protected final String taskStatuses = "//task-status-popup//*[contains(@class, 'ruum-dropdown-option')]";  //  TODO: very similar to commonUtils.dropdownOptions
	protected final String taskDescription = ruumTaskXPath + "//task-description";
	protected final String taskStartDate = ruumTaskXPath + "//button[contains(@class, 'start-date')]";
	protected final String taskDueDate = ruumTaskXPath + "//button[contains(@class, 'due-date')]";
	protected final String taskAction = ruumTaskXPath + "//button[contains(@class, 'py-0')]";
	protected final String taskAssignees = "//task-assignees-popup//*[contains(@class, 'ruum-dropdown-option')]";  //  TODO: very similar to commonUtils.dropdownOptions
	protected final String taskAssigneeAvatar = taskAction + "//ruum-avatar";
	protected final String taskPriority = ruumTaskXPath + "//task-priority";
	protected String chosenStartDate = "";
	protected String chosenDueDate = "";
	RuumPopoverWindow popover = new RuumPopoverWindow();
    
	public RuumSectionTask(){};
	
	public RuumSectionTask(String taskXPath){
		ruumTaskXPath = taskXPath;
	}

    public void select(WebDriver driver) {
    	driver.findElement(By.xpath(ruumTaskXPath)).click();
    }

	public void isExist(WebDriver driver){
		int size = driver.findElements(By.xpath(ruumTaskXPath)).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
	}

    public void enterDates(WebDriver driver) throws Exception {
    	driver.findElement(By.xpath(taskStartDate)).click();
    	driver.findElement(By.xpath(popover.datePickerToday)).click();
        chosenStartDate = CommonUtils.takeChosenDate(driver);  // take the chosen start date
        popover.pushButton(driver, "Select");
        driver.findElement(By.xpath(popover.datePickerNavigationFwd)).click(); // move to next month
        driver.findElement(By.xpath(popover.datePickerDays + "/*[8]")).click();// choose 1st day of 2nd week 
        chosenDueDate = CommonUtils.takeChosenDate(driver);  // take the chosen due date
        popover.pushButton(driver, "Select");
        Thread.sleep(1000);  //  in order to give enough time to Select activity
    }

    public void enterDatesAtPast(WebDriver driver) throws Exception {
    	driver.findElement(By.xpath(taskStartDate)).click();
        //  go two monthes ago and choose startDate
    	driver.findElement(By.xpath(popover.datePickerNavigationBkwd)).click();
    	driver.findElement(By.xpath(popover.datePickerNavigationBkwd)).click();
    	driver.findElement(By.xpath(popover.datePickerDays + "/*[8]")).click();// choose 1st day of 2nd week 
    	chosenStartDate = CommonUtils.takeChosenDate(driver);  // take the chosen start date
    	popover.pushButton(driver, "Select");
        //  go one month ago and choose dueDate
    	driver.findElement(By.xpath(popover.datePickerNavigationBkwd)).click();
    	driver.findElement(By.xpath(popover.datePickerDays + "/*[8]")).click();// choose 1st day of 2nd week 
    	chosenDueDate = CommonUtils.takeChosenDate(driver);  // take the chosen due date
    	popover.pushButton(driver, "Select");
    	Thread.sleep(1000);  //  in order to give enough time to Select activity
    }

    public void toHaveStatus(WebDriver driver, String expectedStatus) {
        Assert.assertTrue(CommonUtils.isElementExists(driver, taskStatusButton + "//*[contains(@class, 'expectedStatus')]"));
    }

    public void assertDates(WebDriver driver) {
        //  replaceAll("(\r\n|\n|\r)","");  // to exclude \n \r (for chrome)
    	String startDate = driver.findElement(By.xpath(taskStartDate)).getText();
    	String dueDate = driver.findElement(By.xpath(taskDueDate)).getText();
    	Assert.assertEquals( startDate.replaceAll("(\r\n|\n|\r)",""),  chosenStartDate);
    	Assert.assertEquals( dueDate.replaceAll("(\r\n|\n|\r)",""),  chosenDueDate);

    }
    public void setStatus(WebDriver driver, String status) {
		driver.findElement(By.xpath(taskStatusButton)).click();
    	driver.findElement(By.xpath(taskStatuses + "//*[contains(text(),'" + status + "')]")).click();
    }

    public void setPriority(WebDriver driver, String priority) {
		driver.findElement(By.xpath(taskPriority)).click();
    	driver.findElement(By.xpath(Params.dropdownOptions + "//*[contains(text(),'" + priority + "')]")).click();
    }

    public void toHavePriority(WebDriver driver, String expectedPriority) {
        // If Priority is not defined, expectedPriority = null.
        if (expectedPriority == null) {
        	Assert.assertTrue("Priority is still defined for the task", driver.findElement(By.xpath(taskPriority)).getAttribute("class").contains("empty"));
        }
        else {
        	Assert.assertTrue(CommonUtils.isElementExists(driver, taskPriority + "//*[contains(@class,'" + expectedPriority + "')]"));
        }
    }

    public void assignPersonToTask(WebDriver driver, String personName) {
		driver.findElement(By.xpath(taskAction)).click();
    	driver.findElement(By.xpath(taskAssignees + "//*[contains(text(),'" + personName + "')]")).click();
        select(driver);  // in order to exit from task-assignees-popup
    }

    public void toHavePersonAssignedToTask(WebDriver driver, String personName, String personAvatar) throws Exception {
        // If nobody is assigned to the task, personAvatar = null.
        if (personAvatar == null) {
        	Assert.assertTrue("User is still assigned to the task", CommonUtils.isElementNotExists(driver, taskAssigneeAvatar));
        }
        else {
        	String actualAvatar = driver.findElement(By.xpath(taskAssigneeAvatar)).getText();
	        actualAvatar = actualAvatar.replaceAll("(\r\n|\n|\r)","");  // to exclude \n \r (for chrome)
	        Assert.assertEquals("User " + personAvatar + " is not assigned to the task", personAvatar,  actualAvatar);
	        Assert.assertTrue("User " + personName + " is not assigned to the task", CommonUtils.isElementExists(driver, taskAssigneeAvatar + "//*[title='" + personName + "']"));
        }
    }
}
