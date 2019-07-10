package my.selenium.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import my.selenium.pageModels.Group;
import my.selenium.pageModels.LoginForm;
import my.selenium.pageModels.Project;
import my.selenium.pageModels.RuumList;
import my.selenium.pageModels.RuumListItem;
import my.selenium.pageModels.RuumNavBar;
import my.selenium.pageModels.RuumSidePanel;
import my.selenium.utils.CommonUtils;
import my.selenium.utils.Params;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Grouping2 {
	private static WebDriver driver;
	private RuumList ruumList = new RuumList();
	private Group group = new Group();
	private Project project = new Project();
	private static LoginForm loginForm = new LoginForm();
	private RuumNavBar navBar = new RuumNavBar();
	private String testProjName = CommonUtils.uniqueTestIdentifier();
	private String testGroupName = CommonUtils.uniqueTestIdentifier();
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver","src/test/java/my/selenium/drivers/chromedriver75/chromedriver.exe");
		driver = new ChromeDriver();
//		driver.manage().timeouts().implicitlyWait(Params.timeOutInSeconds,TimeUnit.SECONDS);
		driver.get(Params.baseURL);
		CommonUtils.resizeBrowser(driver, 1280, 720);
		loginForm.login(driver, Params.personEmails[0], Params.userPassword);
	}

	@Test
	public void test1_inviteNewMembersToGroup() throws Exception {
		ruumList.addNewGroup(driver, testGroupName + "_invite");
	    //  Invite 3 new members
	    for (int i = 1; i < 4; i++) {
	    	group.invitePerson(driver, Params.personEmails[i]);
	    }
	    group.checkUsersNumber(driver, 4);
	    //  remove 1 member
	    group.removePerson(driver, Params.personEmails[2]);
	    group.checkUsersNumber(driver, 3);
	    //  remove 2 other members
	    group.removePerson(driver, Params.personEmails[1]);
	    group.removePerson(driver, Params.personEmails[3]);
	    group.checkUsersNumber(driver, 1);	    
	    
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	}
	
	@Test
	public void test2_createProjectInsideGroup() throws Exception {
		ruumList.addNewGroup(driver, testGroupName + "_projectInside");
	    //  Invite 2 new members
	    for (int i = 1; i < 3; i++) {
	    	group.invitePerson(driver, Params.personEmails[i]);
	    }
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	    RuumListItem listItem = ruumList.getRuumListItemByName(driver, testGroupName + "_projectInside");
	    listItem.isExist(driver);
	    Thread.sleep(1000);  //  sometimes it is required because of page animating
	    listItem.select(driver);
	    Thread.sleep(1000);  //  sometimes it is required because of page animating
	    //  create project
	    ruumList.addNewProjectFromTemplate(driver, "Sales Deal", testProjName + "_insideGroup");
	    project.toHaveRuumTitle(driver, testProjName + "_insideGroup");
	    
	    driver.findElement(By.xpath(project.parentGroupName)).click();
	    listItem = ruumList.getRuumListItemByName(driver, testProjName + "_insideGroup");
	    listItem.isExist(driver);
	    Thread.sleep(1000);  //  sometimes it is required because of page animating
	    listItem.select(driver);
	    Thread.sleep(1000);  //  sometimes it is required because of page animating
	    //  verify the project gets same participants as its group
	    project.checkUsersNumber(driver, 3);
	    project.toHaveNeverAccessed(driver, 2);
	    RuumSidePanel sidePanel = project.openSidePanel(driver, "Team");
	    Assert.assertTrue("participant " + Params.personEmails[0] + " is absent", CommonUtils.isElementExists(driver, sidePanel.teamProjActiveParticipants + "[contains(.,'" + Params.personEmails[0] + "')]"));
	    Assert.assertTrue("participant " + Params.personEmails[1] + " is absent", CommonUtils.isElementExists(driver, sidePanel.teamProjNotVisitedParticipants + "[contains(.,'" + Params.personEmails[1] + "')]"));
	    Assert.assertTrue("participant " + Params.personEmails[2] + " is absent", CommonUtils.isElementExists(driver, sidePanel.teamProjNotVisitedParticipants + "[contains(.,'" + Params.personEmails[2] + "')]"));
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	    //  verify the counter of projects in the group
	    listItem = ruumList.getRuumListItemByName(driver, testGroupName + "_projectInside (1)");
	    listItem.isExist(driver);
	    //  verify the project still exists inside the group
	    listItem.select(driver);
	    listItem = ruumList.getRuumListItemByName(driver, testProjName + "_insideGroup");
	    listItem.isExist(driver);
	    
	    //  both ruum participants leave the group
	    group.removePerson(driver, Params.personEmails[1]);
	    group.removePerson(driver, Params.personEmails[2]);
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	}
	
	@Test
	public void test3_leaveGroup() throws Exception {
		ruumList.leaveRuum(driver, testGroupName + "_invite");
		ruumList.leaveRuum(driver, testGroupName + "_projectInside");
		ruumList.leaveRuum(driver, testProjName + "_insideGroup");
	}
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
