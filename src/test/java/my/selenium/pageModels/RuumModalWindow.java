package my.selenium.pageModels;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import my.selenium.utils.Params;

public class RuumModalWindow {
    protected final String ruumModalWindow = "//ngb-modal-window";
    protected final String inputFields = ruumModalWindow + "//input";
    protected final String buttons = ruumModalWindow + "//button";
    protected final String projectTemplates = "//lobby-template-list//div[contains(@class, 'template-name')]";  // TODO: selector must be improved
    protected final String groupsNames = ruumModalWindow + "//*[contains(@class, 'card-name')]";
    protected final String attachedItems = ruumModalWindow + "//ruum-attachment-item-view";

    protected final String pollQuestionInputField = inputFields + "[@id='question')]";
    protected final String pollOptionsInputFields = inputFields + "[contains(@id, 'poll_option_')]";

    protected final String pollAddOptionButton = ruumModalWindow + "//*[contains(@class, 'plus-button')]";
    protected final String pollDeleteOptionIcons = pollOptionsInputFields + "/following-sibling::[contains(@class, 'delete')]";
    protected final String surveyDialog = ruumModalWindow + "//survey-dialog";

    protected final String mailPreview = ruumModalWindow + "//*[contains(@class, 'ruum-mail')]";
    protected final String mailPreviewSubject = mailPreview + "//*[contains(@class, 'subject')]";
    protected final String mailPreviewText = mailPreview + "//*[contains(@class, 'mail-text')]";

    public void pushButton(WebDriver driver, String buttonCaption) {
    	WebDriverWait wait = new WebDriverWait(driver, Params.timeOutInSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(buttons + "[contains(text(),'" + buttonCaption + "')]")));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttons + "[contains(text(),'" + buttonCaption + "')]"))).click();
    }
    
    public String getProjectTemplate(String templateName) {
    return projectTemplates + "[contains(text(),'" + templateName + "')]/parent::*//button";
}

}
