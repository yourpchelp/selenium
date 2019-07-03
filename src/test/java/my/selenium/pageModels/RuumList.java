package my.selenium.pageModels;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import my.selenium.utils.CommonUtils;
import my.selenium.utils.Params;

public class RuumList {
	protected final long timeOutInSeconds = 60;
    protected final String ruumList = "//lobby-list";
    protected final String emptyState = "//lobby-list//lobby-list-empty-state";
    protected final String ruumListItems = "//*[@data-test='ruum-lobby-list-item-large']";  //  for desktop version only
    public final String newRuumButton = "//lobby-list-new-item//button//*[contains(text(),'New')]";
    protected final String filterButton = "//lobby-list-filters//button//*[contains(text(),'Filters')]";
    protected final String emptyRuumList = "//ruum-list-empty-state";
    protected final String searchButton = "//lobby-list-filters//ruum-search//*[contains(@class, 'icon-search')]";
    protected final String searchCancelButton = "//lobby-list-filters//ruum-search//*[contains(@class, 'icon-cancel')]";
    protected final String searchInputField = "//ruum-search//*[@id='search']";
    protected final String filtersDropdowns = "//lobby-list-filters//ruum-lobby-dropdown";
    protected RuumModalWindow modalWindow = new RuumModalWindow();
    protected RuumPopoverWindow popover = new RuumPopoverWindow();
    protected Project project = new Project();
    protected LoginForm loginForm = new LoginForm();
    protected RuumNavBar navBar = new RuumNavBar();
	
	public void isExist(WebDriver driver){
		int size = driver.findElements(By.xpath(ruumList)).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
	}
	
	public int getNumberOfRuumListItems(WebDriver driver) {
		Boolean isEmpty = driver.findElements(By.xpath(ruumList)).size() == 0;
        if(!isEmpty) {
        return driver.findElements(By.xpath(ruumListItems)).size();
        }
        return 0;
    }
	
    public RuumListItem getRuumListItem(WebDriver driver, int index) {
    	String listItemXPath = "(" + ruumListItems + ")" + "[" + index + "]";
        return new RuumListItem(listItemXPath);
    }
    
    public RuumListItem getRuumListItemByName(WebDriver driver, String name) {
    	String listItemXPath = ruumListItems + "[contains(.,'" + name + "')]";
    	return new RuumListItem(listItemXPath);
    }
    
    public List<String> getRuumListItemNamesAsArray(WebDriver driver) {
        int allItems = getNumberOfRuumListItems(driver);
        List<String> namesArr = new ArrayList<String>();
        for (int j = 0; j < allItems; j++) {
            RuumListItem listItem = getRuumListItem(driver, j);
            namesArr.add(driver.findElement(By.xpath(listItem.ruumListItemName)).getText());
        }
        return namesArr;
    }
    
    public void addNewProjectFromTemplate(WebDriver driver, String templateName, String ruumTitle) throws Exception {
        //  requested template is the "test data",
        //  and it must be prepared before the test.
        //  It must be either in 'Featured' or in 'My Templates' list of templates.
    	driver.findElement(By.xpath(newRuumButton)).click();
    	popover.pushButton(driver, "Ruum from Template");

        String newRuumTemplate = modalWindow.getProjectTemplate(templateName);
        modalWindow.pushButton(driver, "My Templates");
//        driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
        boolean isTemplateAbsent = CommonUtils.isElementNotExists(driver, newRuumTemplate);
//        driver.manage().timeouts().implicitlyWait(Params.timeOutInSeconds,TimeUnit.SECONDS);
        if (isTemplateAbsent){
        	modalWindow.pushButton(driver, "Featured");
        }
        driver.findElement(By.xpath(newRuumTemplate)).click();
        modalWindow.pushButton(driver, "Create Ruum");
        // await t.wait(3000);  //  if Project is created inside Group, Group's name appears in the path with a delay
        project.setRuumTitle(driver, ruumTitle);
            //  the following click is used for exiting from title field
        driver.findElement(By.xpath(project.canvasSyncState)).click();
        Thread.sleep(1000);
    }
    
    public void addNewEmptyProject(WebDriver driver, String ruumTitle) throws Exception {
    	driver.findElement(By.xpath(newRuumButton)).click();
    	popover.pushButton(driver, "Ruum");
    	project.setRuumTitle(driver, ruumTitle);
        //  the following click is used for exiting from title field
    	driver.findElement(By.xpath(project.canvasSyncState)).click();
        Thread.sleep(1000);
    }
    
    public void addNewGroup(WebDriver driver, String groupName) throws Exception {
    	driver.findElement(By.xpath(newRuumButton)).click();
    	driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Project Group')]")).click();
    	driver.findElement(By.xpath(modalWindow.inputFields)).click(); // only one input field exists now
    	driver.findElement(By.xpath(modalWindow.inputFields)).clear();
    	driver.findElement(By.xpath(modalWindow.inputFields)).sendKeys(groupName);
    	modalWindow.pushButton(driver, "Create");
    	Thread.sleep(1000);
    }
    
    public void leaveRuum(WebDriver driver, String ruumTitle) throws Exception {
        RuumListItem listItem = getRuumListItemByName(driver, ruumTitle);
        listItem.isExist(driver);
        driver.findElement(By.xpath(listItem.ruumListItemOptions)).click();
        popover.pushButton(driver, "Leave");
        modalWindow.pushButton(driver, "Cancel");
        listItem.isExist(driver);
        driver.findElement(By.xpath(listItem.ruumListItemOptions)).click();
        popover.pushButton(driver, "Leave");
        modalWindow.pushButton(driver, "Leave");
        Thread.sleep(1000);
        listItem.isNotExist(driver);
    }
    
    public void archiveRuum(WebDriver driver, String ruumTitle) {
        filterRuumsByState(driver, "Active");
        toShowRuumsByCorrectState(driver, "Active");
        RuumListItem listItem = getRuumListItemByName(driver, ruumTitle);
        driver.findElement(By.xpath(listItem.ruumListItemOptions)).click();
        driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Archive')]")).click();
        if (CommonUtils.isElementExists(driver, modalWindow.surveyDialog)){
        	Actions action = new Actions(driver);
        	action.sendKeys(Keys.ESCAPE).build().perform();
        }
        if (CommonUtils.isElementExists(driver, modalWindow.ruumModalWindow)){
            modalWindow.pushButton(driver, "Archive");
        }
        listItem.isNotExist(driver);
    }

    public void unarchiveRuum(WebDriver driver, String ruumTitle) {
    	filterRuumsByState(driver, "Archived");
    	toShowRuumsByCorrectState(driver, "Archived");
    	RuumListItem listItem = getRuumListItemByName(driver, ruumTitle);
        driver.findElement(By.xpath(listItem.ruumListItemOptions)).click();
        driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Un-Archive')]")).click();
        listItem.isNotExist(driver);
    }

    public void filterRuumsByType(WebDriver driver, String ruumsType) {
        //  ruumsType can be "Groups & Projects", "Groups" or "Projects"
    	driver.findElements(By.xpath(filtersDropdowns)).get(1).click();
    	popover.isExist(driver);
    	driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[text(), '" + ruumsType + "']")).click();
    }

    public void filterRuumsByState(WebDriver driver, String ruumsState) {
        //  ruumsState can be "Active" or "Archived"
        int index = 2;
        int size = driver.findElements(By.xpath("//ruum-project-group-projects")).size();
        boolean insideGroup = size == 1;
        if(insideGroup) {
            index = 1;
        }
        driver.findElements(By.xpath(filtersDropdowns)).get(index).click();
        popover.isExist(driver);
        driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[text(), '" + ruumsState + "']")).click();
    }

    public void toShowRuumsByCorrectState(WebDriver driver, String ruumsState){
        int index = 2;
        int size = driver.findElements(By.xpath("//ruum-project-group-projects")).size();
        boolean insideGroup = size == 1;
        if(insideGroup) {
            index = 1;
        }
        String xPath = "(" + filtersDropdowns + ")[" + index + "]" + "//*[contains(text(), '" + ruumsState + "')]";
        Assert.assertTrue("Ruum List must show " + ruumsState, CommonUtils.isElementExists(driver, xPath));
    }

    public void toShowRuumsByCorrectType(WebDriver driver, String ruumType){
    	String xPath = "(" + filtersDropdowns + ")[" + 1 + "]" + "//*[contains(text(), '" + ruumType + "')]";
        Assert.assertTrue("Ruum List must show " + ruumType, CommonUtils.isElementExists(driver, xPath));
    }

    public void toHaveFilterStatusActive(WebDriver driver){
    	Assert.assertTrue("Filter button was not pressed", driver.findElement(By.xpath(filterButton)).getAttribute("class").contains("btn-link-primary"));
    	Assert.assertTrue("status filter bar is absent", CommonUtils.isElementExists(driver, "//lobby-list-filters//lobby-list-status-filter"));
    }

    public void toHaveFilterLabelActive(WebDriver driver){
    	Assert.assertTrue("Filter button was not pressed", driver.findElement(By.xpath(filterButton)).getAttribute("class").contains("btn-link-primary"));
    	Assert.assertTrue("label filter bar is absent", CommonUtils.isElementExists(driver, "//lobby-list-filters//lobby-list-tags-filter"));
    }

    public void toHaveFilterInactive(WebDriver driver){
    	Assert.assertFalse("Filter button is still pressed", driver.findElement(By.xpath(filterButton)).getAttribute("class").contains("btn-link-primary"));
    	Assert.assertFalse("status filter bar must be absent", CommonUtils.isElementExists(driver, "//lobby-list-filters//lobby-list-status-filter"));
    	Assert.assertFalse("label filter bar must be absent", CommonUtils.isElementExists(driver, "//lobby-list-filters//lobby-list-tags-filter"));
    }

    public void clickFilterOption(WebDriver driver, String filterBy, String filterOption) {
        //  filterBy can be "Status" or "Label"
        switch(filterBy) {
            case "Status":
            	driver.findElement(By.xpath("//lobby-list-filters//lobby-list-status-filter//ruum-status-button" + "//*[contains(text(),'" + filterOption + "')]")).click();
                break;
            case "Label":
            	driver.findElement(By.xpath("//lobby-list-filters//lobby-list-tags-filter//ruum-tag" + "//*[contains(text(),'" + filterOption + "')]")).click();
                break;
            default:
            	Assert.fail("Wrong filterBy parameter");
                break;
        }
    }

    public void toHaveFilterOptionActive(WebDriver driver, String filterBy, String filterOption) {
        //  filterBy can be "Status" or "Label"
        switch(filterBy) {
            case "Status":
            	String xPathStatus = "//lobby-list-filters//lobby-list-status-filter//ruum-status-button" + "//*[contains(text(),'" + filterOption + "')]" + "//button/*[1]";
            	Assert.assertFalse("filter option " + filterOption + " must be chosen", driver.findElement(By.xpath(xPathStatus)).getAttribute("class").contains("text-dark"));
                break;
            case "Label":
            	String xPathLabel = "//lobby-list-filters//lobby-list-tags-filter//ruum-tag" + "//*[contains(text(),'" + filterOption + "')]" + "//button/*[1]";
            	Assert.assertFalse("filter option " + filterOption + " must be chosen", driver.findElement(By.xpath(xPathLabel)).getAttribute("class").contains("text-dark"));
                break;
            default:
            	Assert.fail("Wrong filterBy parameter");
                break;
        }
    }
    
    public void toHaveFilterOptionInactive(WebDriver driver, String filterBy, String filterOption) {
        //  filterBy can be "Status" or "Label"
        switch(filterBy) {
            case "Status":
            	String xPathStatus = "//lobby-list-filters//lobby-list-status-filter//ruum-status-button" + "//*[contains(text(),'" + filterOption + "')]" + "//button/*[1]";
            	Assert.assertTrue("filter option " + filterOption + " must not be chosen", driver.findElement(By.xpath(xPathStatus)).getAttribute("class").contains("text-dark"));
                break;
            case "Label":
            	String xPathLabel = "//lobby-list-filters//lobby-list-tags-filter//ruum-tag" + "//*[contains(text(),'" + filterOption + "')]" + "//button/*[1]";
            	Assert.assertTrue("filter option " + filterOption + " must not be chosen", driver.findElement(By.xpath(xPathLabel)).getAttribute("class").contains("text-dark"));
                break;
            default:
            	Assert.fail("Wrong filterBy parameter");
                break;
        }
    }

    public void waitIfBusy(WebDriver driver){
        boolean isBusy = false;
		do {
        	isBusy = CommonUtils.isElementExists(driver, Params.busyState);
        }
        while (isBusy) ;
    }
    
    public void waitIfNotBusy(WebDriver driver){
        boolean isBusy = true;
		do {
        	isBusy = CommonUtils.isElementExists(driver, Params.busyState);
        }
        while (isBusy == false) ;
    }

    public void searchRuumsByText(WebDriver driver, String searchText) throws Exception {
    	String name = "";
        String re = "(?i).*" + searchText + ".*";  //  regex with ignorecase
        //  get the number of all items before searching
        int allItems = getNumberOfRuumListItems(driver);
        //  get the number of suitable items before searching
        int itemsBefore = 0;
        for (int j = 0; j < allItems; j++) {
            RuumListItem listItem = getRuumListItem(driver, j);
            name = driver.findElement(By.xpath(listItem.ruumListItemName)).getText();
            if (name.matches(re)){
                itemsBefore++;
            }
        }
        //  start the search
        driver.findElement(By.xpath(searchButton)).click();
        driver.findElement(By.xpath(searchInputField)).sendKeys(searchText);
        Thread.sleep(3000);
        // await this.waitIfNotBusy();
        waitIfBusy(driver);
        //  get the number of all items after searching
        int itemsAfter = getNumberOfRuumListItems(driver);
        Assert.assertEquals("Search returns the wrong number of ruums", itemsBefore, itemsAfter);
        //  verify, all found items meet the search criteria
        for (int j = 0; j < itemsAfter; j++) {
        	RuumListItem listItem = getRuumListItem(driver, j);
        	name = driver.findElement(By.xpath(listItem.ruumListItemName)).getText();
        	Assert.assertTrue("ruum " + name + " does not meet the search criteria", name.matches(re));
        }
        // cancel search
        driver.findElement(By.xpath(searchCancelButton)).click();
        Thread.sleep(2000);
        Assert.assertFalse("Search input field must disappear.", CommonUtils.isElementExists(driver, searchInputField));
        int itemsAfterCancelling = getNumberOfRuumListItems(driver);
        Assert.assertEquals("wrong number of ruums after search cancelling", allItems, itemsAfterCancelling);
    }

    public void renameRuum(WebDriver driver, String currentTitle, String newTitle) {
        RuumListItem listItem = getRuumListItemByName(driver, currentTitle);
        listItem.isExist(driver);
        driver.findElements(By.xpath(listItem.ruumListItemXPath + "//*[contains(@class, 'ruum-lobby-item-more-options')]//button")).get(1).click();
        driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Rename')]")).click();
    	driver.findElement(By.xpath("//ruum-modal-dialog//form//input")).clear();
    	driver.findElement(By.xpath("//ruum-modal-dialog//form//input")).sendKeys(newTitle);
    	driver.findElement(By.xpath("//ruum-modal-dialog//form//button[contains(text(),'Update')]")).click();
    	getRuumListItemByName(driver, newTitle).isExist(driver);
    }

    public void generateProjectReport(WebDriver driver, String projectName) throws Exception {
    	RuumListItem listItem = getRuumListItemByName(driver, projectName);
    	driver.findElements(By.xpath(listItem.ruumListItemXPath + "//*[contains(@class, 'ruum-lobby-item-more-options')]//button")).get(1).click();
        driver.findElement(By.xpath(popover.getRuumPopoverOptions() + "//*[contains(text(),'Generate Project Report')]")).click();
        Thread.sleep(1000);
        Assert.assertTrue(CommonUtils.isElementExists(driver, "//ruum-report//ruum-navbar//ruum-main-navbar//ruum-report-breadcrumb-navbar//*[contains(text(),'Report')]"));
    }
}
