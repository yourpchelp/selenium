package pageModels;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
    	driver.findElement(By.xpath(buttons + "[contains(text(),'" + buttonCaption + "')]")).click();
    }
    
    public String getProjectTemplate(String templateName) {
    return projectTemplates + "[contains(text(),'" + templateName + "')]/parent//button";
}

}
