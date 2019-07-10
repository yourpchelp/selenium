package my.selenium.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import my.selenium.pageModels.Group;
import my.selenium.pageModels.LoginForm;
import my.selenium.pageModels.RuumList;
import my.selenium.pageModels.RuumListItem;
import my.selenium.pageModels.RuumNavBar;
import my.selenium.utils.CommonUtils;
import my.selenium.utils.Params;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Grouping1 {
	private static WebDriver driver;
	private RuumList ruumList = new RuumList();
	private Group group = new Group();
	private static LoginForm loginForm = new LoginForm();
	private RuumNavBar navBar = new RuumNavBar();
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
	public void test1_createGroup() throws Exception {
	    ruumList.addNewGroup(driver, testGroupName + "_new");
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	    RuumListItem listItem = ruumList.getRuumListItemByName(driver, testGroupName + "_new");
	    listItem.isExist(driver);
	}
	
	@Test
	public void test2_renameGroupFromInside() throws Exception {
	    ruumList.addNewGroup(driver, testGroupName + "_toRename");
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	    RuumListItem listItem = ruumList.getRuumListItemByName(driver, testGroupName + "_toRename");
	    listItem.isExist(driver);
	    Thread.sleep(1000);  //  sometimes it is required because of page animating
	    listItem.select(driver);
		Thread.sleep(2000);  //  sometimes it is required because of page animating
	    group.renameGroupFromInsideIt(driver, testGroupName + "_renamed");
	    listItem = ruumList.getRuumListItemByName(driver, testGroupName + "_renamed");
	    listItem.isExist(driver);
	}
	
	@Test
	public void test3_renameGroupFromOutside() throws Exception {
		ruumList.addNewGroup(driver, testGroupName + "_toRenameOutside");
		driver.findElement(By.xpath(navBar.ruumLogo)).click();
		RuumListItem listItem = ruumList.getRuumListItemByName(driver, testGroupName + "_toRenameOutside");
		listItem.isExist(driver);
		Thread.sleep(1000);  //  sometimes it is required because of page animating
		listItem.renameRuum(driver, testGroupName + "_renamedFromOutside");
	    driver.findElement(By.xpath(navBar.ruumLogo)).click();
	    listItem = ruumList.getRuumListItemByName(driver, testGroupName + "_renamedFromOutside");
	    listItem.isExist(driver);
	}
	
	@Test
	public void test4_leaveGroup() throws Exception {
		ruumList.leaveRuum(driver, testGroupName + "_new");
		ruumList.leaveRuum(driver, testGroupName + "_renamedFromOutside");
		ruumList.leaveRuum(driver, testGroupName + "_renamed");
	}
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
