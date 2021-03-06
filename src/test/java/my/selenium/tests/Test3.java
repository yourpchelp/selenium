package my.selenium.tests;

import org.junit.*;
import org.openqa.selenium.*;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
// @Ignore
public class Test3 {
	private WebDriver driver;
	private String baseURL = "http://demo.guru99.com/test/newtours/register.php";

	@Before
	public void setUp() throws Exception {
//		System.setProperty("webdriver.gecko.driver","src/test/java/my/selenium/drivers/geckodriver");
//		driver = new FirefoxDriver();
//		System.setProperty("webdriver.chrome.driver","src/test/java/my/selenium/drivers/chromedriver");
		System.setProperty("webdriver.chrome.driver","src/test/java/my/selenium/drivers/chromedriver.exe");
//		ChromeOptions chromeOptions = new ChromeOptions();
//		chromeOptions.setBinary("/bin/google-chrome");
//		chromeOptions.setBinary("/usr/bin/google-chrome");
//		chromeOptions.setBinary("/opt/google/chrome/chrome");
//		chromeOptions.addArguments("--headless");
//		chromeOptions.setBinary("/path/to/other/chrome/binary");
		driver = new ChromeDriver();
	}

	
	@Test
	public void testA() throws InterruptedException {	    
		driver.get(baseURL);

		Select drpCountry = new Select(driver.findElement(By.name("country")));
		drpCountry.selectByVisibleText("ANTARCTICA");

		//Selecting Items in a Multiple SELECT elements
		driver.get("http://jsbin.com/osebed/2");
		Select fruits = new Select(driver.findElement(By.id("fruits")));
		fruits.selectByVisibleText("Banana");
		fruits.selectByIndex(1);
	}
	
	@Test
	public void testB() throws InterruptedException {	    
		driver.get(baseURL);

		Select drpCountry = new Select(driver.findElement(By.name("country")));
		drpCountry.selectByVisibleText("ANTARCTICA");

		//Selecting Items in a Multiple SELECT elements
		driver.get("http://jsbin.com/osebed/2");
		Select fruits = new Select(driver.findElement(By.id("fruits")));
		fruits.selectByVisibleText("Banana");
		fruits.selectByIndex(1);
	}
	
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

}
