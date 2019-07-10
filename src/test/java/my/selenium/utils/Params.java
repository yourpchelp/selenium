package my.selenium.utils;

public class Params {
	public static final String baseURL = "https://open.staging-ruumapp.com";
//	public static final String baseURL = "https://open.ruumapp.com";
    public static final String[] personEmails = {
         "endtoendtestuser@loadtest.ruumapp.com",
         "endtoendtestuser1@loadtest.ruumapp.com",
         "endtoendtestuser2@loadtest.ruumapp.com",
         "endtoendtestuser3@loadtest.ruumapp.com"
    };
     public static final String[] personNames = {"End To End Test User", "EndToEnd User", "EndToEnd User", "EndToEnd User"};
     public static final String[] personAvatars = {"ETE", "EU"};
     public static final String userPassword = "12345678";

     public static final String[] ruumTypes = {"Groups & Projects\nGroups and solo projects", "Groups\nAll groups", "Projects\nAll projects"}; //  TODO: dive "data-test" attribute for filters
     public static final String[] ruumStates = {"Active", "Archived"};
     
     public static final String dropdownOptions = "//ruum-dropdown-popup//*[contains(@class, 'ruum-dropdown-option')]";
     public final static String busyState = "//busy-state//*[contains(@class, 'busy-container')]" + "//*[contains(@class, 'busy')]";
     
     public static final int timeOutInSeconds = 30;
}
