package my.selenium.tests;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class testRuum {
	private WebDriver driver;
	private String baseURL = "https://open.staging-ruumapp.com";
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver","src/test/java/my/selenium/drivers/chromedriver74/chromedriver.exe");
		driver = new ChromeDriver();
	}

	@Test
	public void test() throws Exception {
		driver.get(baseURL);
//		Thread.sleep(60000);
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@data-test='ruum-auth-email-input']")));
//		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//input[@data-test='ruum-auth-email-input']"))));
		
		int size = driver.findElements(By.xpath("//input[@data-test='ruum-auth-email-input']")).size();
		Assert.assertTrue(String.valueOf(size), size == 1);
		
		Assert.assertTrue(driver.findElement(By.xpath("//button[@data-test='ruum-auth-signin-button']")).isDisplayed());
		driver.findElement(By.xpath("//input[@data-test='ruum-auth-email-input']")).sendKeys("endtoendtestuser@loadtest.ruumapp.com");

		Assert.assertTrue(driver.findElement(By.xpath("//button[@data-test='ruum-auth-signin-button']")).isEnabled());
		driver.findElement(By.xpath("//button[@data-test='ruum-auth-signin-button']")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@data-test='ruum-auth-password-input']")));
		
		Assert.assertTrue(driver.findElement(By.xpath("//input[@data-test='ruum-auth-password-input']")).isEnabled());
		driver.findElement(By.xpath("//input[@data-test='ruum-auth-password-input']")).sendKeys("12345678");
		Assert.assertTrue(driver.findElement(By.xpath("//button[@data-test='ruum-auth-signin-button']")).isEnabled());
		Assert.assertTrue(driver.findElement(By.xpath("//button[@data-test='ruum-auth-signin-button']")).getAttribute("innerText").equals("Login"));
		driver.findElement(By.xpath("//button[@data-test='ruum-auth-signin-button']")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//lobby-list-new-item//button//*[text() = 'New']")));
		Thread.sleep(6000);
		
//		fail("Not yet implemented");
	}
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
