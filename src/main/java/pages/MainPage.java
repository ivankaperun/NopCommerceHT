package pages;

import org.openqa.selenium.support.ui.ExpectedConditions;


public class MainPage extends AbstractPage {
    public MainPage() {
        super();
    }

    public MainPage openMainPage() {
        wait.until(ExpectedConditions.elementToBeClickable(computersTitle));
        waitUntilPageIsFullyLoaded(wait);
        perfNavigationTiming.writeToInflux("MainPage");

    return this;
    }
}
