package my.selenium.tests;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import my.selenium.utils.*;
import my.selenium.pageModels.*;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Login{
	private WebDriver driver;
	private WebDriverWait wait;
	protected RuumList ruumList = new RuumList();
	protected LoginForm loginForm = new LoginForm();
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver","src/test/java/my/selenium/drivers/chromedriver75/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get(Params.baseURL);
		CommonUtils.resizeBrowser(driver, 1280, 720);
		wait = new WebDriverWait(driver, Params.timeOutInSeconds);
	}

	@Test
	public void testLogin() throws Exception {

		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(loginForm.emailInputField)));
//		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(this.emailInputField))));
		
		int size = driver.findElements(By.xpath(loginForm.emailInputField)).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
		
		Assert.assertTrue(driver.findElement(By.xpath(loginForm.nextButton)).isDisplayed());
		driver.findElement(By.xpath(loginForm.emailInputField)).sendKeys(Params.personEmails[0]);

		Assert.assertTrue(driver.findElement(By.xpath(loginForm.nextButton)).isEnabled());
		driver.findElement(By.xpath(loginForm.nextButton)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(loginForm.passwordInputField)));
		
		Assert.assertTrue(driver.findElement(By.xpath(loginForm.passwordInputField)).isEnabled());
		driver.findElement(By.xpath(loginForm.passwordInputField)).sendKeys(Params.userPassword);
		Assert.assertTrue(driver.findElement(By.xpath(loginForm.nextButton)).isEnabled());
		Assert.assertTrue(driver.findElement(By.xpath(loginForm.nextButton)).getAttribute("innerText").equals("Login"));
		driver.findElement(By.xpath(loginForm.nextButton)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ruumList.newRuumButton)));
		Thread.sleep(6000);
		
//		fail("Not yet implemented");
	}
	
	@Test
	public void testLoginFunc() throws Exception {
		loginForm.login(driver, Params.personEmails[0], Params.userPassword);
	}
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
