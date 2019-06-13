package my.selenium.tests;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import my.selenium.utils.CommonUtils;
import my.selenium.utils.Params;
import pageModels.LoginForm;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class testRuum extends LoginForm{
	private WebDriver driver;
	private WebDriverWait wait;
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver","src/test/java/my/selenium/drivers/chromedriver74/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get(Params.baseURL);
		CommonUtils.resizeBrowser(driver, 1280, 720);
		wait = new WebDriverWait(driver, timeOutInSeconds);
	}

	@Test
	public void testLogin() throws Exception {

		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(emailInputField)));
//		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(this.emailInputField))));
		
		int size = driver.findElements(By.xpath(emailInputField)).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
		
		Assert.assertTrue(driver.findElement(By.xpath(nextButton)).isDisplayed());
		driver.findElement(By.xpath(emailInputField)).sendKeys(Params.personEmails[0]);

		Assert.assertTrue(driver.findElement(By.xpath(nextButton)).isEnabled());
		driver.findElement(By.xpath(nextButton)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(passwordInputField)));
		
		Assert.assertTrue(driver.findElement(By.xpath(passwordInputField)).isEnabled());
		driver.findElement(By.xpath(passwordInputField)).sendKeys(Params.userPassword);
		Assert.assertTrue(driver.findElement(By.xpath(nextButton)).isEnabled());
		Assert.assertTrue(driver.findElement(By.xpath(nextButton)).getAttribute("innerText").equals("Login"));
		driver.findElement(By.xpath(nextButton)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//lobby-list-new-item//button//*[text() = 'New']")));
		Thread.sleep(6000);
		
//		fail("Not yet implemented");
	}
	
	@Test
	public void testLoginFunc() throws Exception {
		login(driver);
	}
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
