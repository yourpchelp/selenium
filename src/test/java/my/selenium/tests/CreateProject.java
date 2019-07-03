package my.selenium.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import my.selenium.pageModels.LoginForm;
import my.selenium.pageModels.Project;
import my.selenium.pageModels.RuumList;
import my.selenium.pageModels.RuumListItem;
import my.selenium.pageModels.RuumNavBar;
import my.selenium.utils.CommonUtils;
import my.selenium.utils.Params;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateProject {
	private static WebDriver driver;
	private RuumList ruumList = new RuumList();
	private Project project = new Project();
	private static LoginForm loginForm = new LoginForm();
	private RuumNavBar navBar = new RuumNavBar();
	private String testProjName = CommonUtils.uniqueTestIdentifier();
	
	@BeforeClass
	public static void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver","src/test/java/my/selenium/drivers/chromedriver75/chromedriver.exe");
		driver = new ChromeDriver();
//		driver.manage().timeouts().implicitlyWait(Params.timeOutInSeconds,TimeUnit.SECONDS);
		driver.get(Params.baseURL);
		CommonUtils.resizeBrowser(driver, 1280, 720);
		loginForm.login(driver, Params.personEmails[0], Params.userPassword);
	}

	@Test
	public void test1_createNewEmptyProject() throws Exception {
	    ruumList.addNewEmptyProject(driver, testProjName + "_empty");
	    project.toHaveRuumTitle(driver, testProjName + "_empty");
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	    RuumListItem listItem = ruumList.getRuumListItemByName(driver, testProjName + "_empty");
	    listItem.isExist(driver);
	}
	
	@Test
	public void test2__createNewProjectFromTemplate() throws Exception {
	    ruumList.addNewProjectFromTemplate(driver, "Sales Deal", testProjName + "_template");
	    project.toHaveRuumTitle(driver, testProjName + "_template");
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	    RuumListItem listItem = ruumList.getRuumListItemByName(driver, testProjName + "_template");
	    listItem.isExist(driver);
	}
	
	@Test
	public void test3_editProjectTitleFromInsideProject() throws Exception {
		ruumList.addNewProjectFromTemplate(driver, "Sales Deal", testProjName);
		driver.findElement(By.xpath(navBar.ruumLogo)).click();
	    RuumListItem listItem = ruumList.getRuumListItemByName(driver, testProjName);
	    listItem.select(driver);
	    project.setRuumTitle(driver, testProjName + "_title");
	    //  the following click is used for exiting from title field
        driver.findElement(By.xpath(project.canvasSyncState)).click();
        project.toHaveRuumTitle(driver, testProjName + "_title");
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	    listItem = ruumList.getRuumListItemByName(driver, testProjName + "_title");
	    listItem.isExist(driver);
	}

	@Test
	public void test4_renameProjectFromOutside() throws Exception {
		ruumList.addNewEmptyProject(driver, testProjName + "_toRename");
		driver.findElement(By.xpath(navBar.ruumLogo)).click();
		ruumList.renameRuum(driver, testProjName + "_toRename", testProjName + "_renamed");
	}
	
	@Test
	public void test5_inviteNewMembersToProject() throws Exception {
		ruumList.addNewProjectFromTemplate(driver, "Sales Deal", testProjName + "_invite");
		driver.findElement(By.xpath(navBar.ruumLogo)).click();
		RuumListItem listItem = ruumList.getRuumListItemByName(driver, testProjName + "_invite");
		listItem.select(driver);
		Thread.sleep(1000);
		
		project.checkUsersNumber(driver, 1);
		
	    //  Invite 3 new members
	    for (int i = 1; i < 4; i++) {
	        project.invitePerson(driver, Params.personEmails[i]);
	    }
	    
	    project.checkUsersNumber(driver, 4);
	    
	    //  remove 1 member
	    project.removePerson(driver, Params.personEmails[2]);
	    project.checkUsersNumber(driver, 3);

	    // check number of Never Accessed
	    project.toHaveNeverAccessed(driver, 2);
	        //  logout from current user
	        //  login and visit the ruum with other invited user
	        //  logout from this user
	        //  login with previous user again and see changes in "Never Accessed"
	    for (int i = 1; i > -1; i--) {
	    	driver.findElement(By.xpath(navBar.ruumLogo)).click();
	    	Thread.sleep(1000);
	    	navBar.logout(driver);
	        loginForm.login(driver, Params.personEmails[i],Params.userPassword);
			listItem.select(driver);
			Thread.sleep(1000);
	    }
	    project.toHaveNeverAccessed(driver, 1);
	    
	    //  remove 2 other members
	    project.removePerson(driver, Params.personEmails[1]);
	    project.removePerson(driver, Params.personEmails[3]);
	    project.checkUsersNumber(driver, 1);	    
	    
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	}
	
	@Test
	public void test6_leaveProject() throws Exception {
		ruumList.leaveRuum(driver, testProjName + "_empty");
		ruumList.leaveRuum(driver, testProjName + "_template");
		ruumList.leaveRuum(driver, testProjName + "_renamed");
		ruumList.leaveRuum(driver, testProjName + "_invite");
		ruumList.leaveRuum(driver, testProjName + "_title");
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		driver.quit();
	}

}
