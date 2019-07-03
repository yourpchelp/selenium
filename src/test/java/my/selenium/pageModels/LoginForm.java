package my.selenium.pageModels;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import my.selenium.utils.Params;

public class LoginForm {
//	protected final String loginForm = "//*[contains(@class, 'form-group')]";
	public final String nextButton = "//button[@data-test='ruum-auth-signin-button']";
	public final String emailInputField = "//input[@data-test='ruum-auth-email-input']";
	public final String passwordInputField = "//input[@data-test='ruum-auth-password-input']";
	protected RuumNavBar navBar = new RuumNavBar();
	
	public void login(WebDriver driver, String email, String password) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, Params.timeOutInSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(emailInputField)));
//		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(this.emailInputField))));
		driver.findElement(By.xpath(emailInputField)).clear();
		driver.findElement(By.xpath(emailInputField)).sendKeys(email);
		driver.findElement(By.xpath(nextButton)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(passwordInputField)));
		driver.findElement(By.xpath(passwordInputField)).sendKeys(password);
		driver.findElement(By.xpath(nextButton)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(navBar.ruumNavBar)));
	}
}
