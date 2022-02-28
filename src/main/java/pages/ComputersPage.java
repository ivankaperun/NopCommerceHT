package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import util.WebDriverHolder;


public class ComputersPage extends AbstractPage {
    public ComputersPage() {
        super();
    }

    private static final String EXPECTED_URL_TEXT = "computers";

    @FindBy(xpath = "//div[@class='page category-page']")
    private WebElement popularCategoriesBanner;

    public ComputersPage openComputersPage() {
        computersTitle.click();
        wait.until(ExpectedConditions.visibilityOf(popularCategoriesBanner));
        waitUntilPageIsFullyLoaded(wait);
        Assert.assertTrue(WebDriverHolder.getDriver().getCurrentUrl().contains(EXPECTED_URL_TEXT));
        perfNavigationTiming.writeToInflux("ComputersPage");
        return this;
    }
}
