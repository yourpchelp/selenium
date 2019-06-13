package pageModels;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import my.selenium.utils.Params;

public class LoginForm {
	protected static final String loginForm = "//*[contains(@class, 'form-group')]";
	protected static final String nextButton = "//button[@data-test='ruum-auth-signin-button']";
	protected static final String emailInputField = "//input[@data-test='ruum-auth-email-input']";
	protected static final String passwordInputField = "//input[@data-test='ruum-auth-password-input']";
	protected static final long timeOutInSeconds = 60;
	
	public static void login(WebDriver driver) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(emailInputField)));
//		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(this.emailInputField))));
		driver.findElement(By.xpath(emailInputField)).sendKeys(Params.personEmails[0]);
		driver.findElement(By.xpath(nextButton)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(passwordInputField)));
		driver.findElement(By.xpath(passwordInputField)).sendKeys(Params.userPassword);
		driver.findElement(By.xpath(nextButton)).click();
	}
}
