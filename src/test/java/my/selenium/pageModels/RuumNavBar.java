package my.selenium.pageModels;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RuumNavBar {
	protected final String ruumNavBar = "//ruum-navbar";
	protected final String ruumInviteButton = "//ruum-navbar//button[contains(.,'Invite')]";
    protected final String userProfileButton = "//ruum-navbar//button[@data-test='ruum-navbar-profile']";
    protected final String templatesButton = "//ruum-navbar//ruum-main-navbar//*[@class='nav-item']//button[contains(text(),'Templates')]";
    public final String ruumLogo = "//ruum-navbar//ruum-main-navbar//*[contains(@class, 'navbar-brand')]";
    protected final String taskListButton = "//ruum-navbar//button//*[contains(text(),'Task List')]";
    protected final String canvasButton = "//ruum-navbar//button//*[contains(text(),'Canvas')]";
    protected final String projectTemplatesButton = "//ruum-navbar//button//*[contains(text(),'Project Templates')]";
    protected final String ruumTitle = "//ruum-navbar//*[contains(@class, 'ruum-breadcrumb-active-item')]";  //  TODO: At first, this selector points on groupName, and only after the delay is begins to point on projectName ("New Project")
    protected RuumPopoverWindow popover = new RuumPopoverWindow();
    
    public WebElement getSidePanelButton(WebDriver driver, String titleButton) {
    	// titleButton can be Team, Tasks, Files, Activities, Emails, Variables or Info
        return driver.findElement(By.xpath("//button[@title='" + titleButton + "']"));
    }
    
    public void logout(WebDriver driver) {
        driver.findElement(By.xpath(userProfileButton)).click();
        popover.pushButton(driver, "Logout");
    }
}
