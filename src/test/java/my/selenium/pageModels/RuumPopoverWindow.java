package my.selenium.pageModels;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RuumPopoverWindow {
    protected final String ruumPopoverWindow = "//ngb-popover-window";
    protected final String ruumPopoverOptions = ruumPopoverWindow + "//*[contains(@class, 'popover-body')]//ruum-dropdown-list//ruum-dropdown-item";
    protected final String ruumPopoverStatuses = ruumPopoverWindow + "//*[contains(@class, 'popover-body')]//ruum-status-button";
    protected final String ruumPopoverInput = ruumPopoverWindow + "//*[contains(@class, 'popover-body')]//form//input";
    protected final String ruumPopoverButtons = ruumPopoverWindow + "//*[contains(@class, 'popover-body')]//button";
    protected final String ruumPopoverRuumTags = ruumPopoverWindow + "//*[contains(@class, 'popover-body')]//form//ruum-tag";

    protected final String datePicker = ruumPopoverWindow + "//*[contains(@class, 'ruum-datepicker')]";
    protected final String datePickerWeeks = datePicker + "//*[contains(@class, 'ruum-datepicker-week')]";
    protected final String datePickerDays = datePickerWeeks + "//*[contains(@class, 'ruum-datepicker-day')]";
    protected final String datePickerToday = datePickerDays + "//*[contains(@class, 'ruum-datepicker-today')]";
    public final String datePickerSelectedDay = datePickerDays + "//*[contains(@class, 'ruum-datepicker-selected-day')]";
    public final String datePickerNavigation = datePicker + "//*[contains(@class, 'ruum-datepicker-navigation')]";
    protected final String datePickerNavigationFwd = datePickerNavigation + "//button//*[contains(@class, 'icon-forward')]";
    protected final String datePickerNavigationBkwd = datePickerNavigation + "//button//*[contains(@class, 'icon-back')]";

    public String getRuumPopoverOptions() {
        return ruumPopoverOptions;
    }
    
    public void pushButton(WebDriver driver, String buttonCaption) {
    	driver.findElement(By.xpath(ruumPopoverButtons + "[contains(.,'" + buttonCaption + "')]")).click();
    }
    
	public void isExist(WebDriver driver){
		int size = driver.findElements(By.xpath(ruumPopoverWindow)).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
	}
}
