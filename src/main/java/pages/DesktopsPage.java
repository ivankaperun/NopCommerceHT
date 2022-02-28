package pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import util.WebDriverHolder;

import java.util.List;


public class DesktopsPage extends AbstractPage {

    public DesktopsPage() {
        super();
    }

    private static final String EXPECTED_URL_TEXT = "desktops";

    @FindBy(xpath = "//div[@class='picture']//a[@href='/desktops']")
    private WebElement desktopsPageButton;

    @FindBy(xpath = "//h2[@class='product-title']//a")
    private List<WebElement> desktopsProductList;

    @FindBy(xpath = "//div[@class='product-name']//h1")
    private WebElement openedDesktopTitle;

    @FindBy(xpath = "//button[@id='add-to-cart-button-1']")
    private WebElement addToCartButton;

    public DesktopsPage openDesktopsPage() {
        wait.until(ExpectedConditions.elementToBeClickable(desktopsPageButton));
        desktopsPageButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(desktopsProductList.get(0)));
        waitUntilPageIsFullyLoaded(wait);
        Assert.assertTrue(WebDriverHolder.getDriver().getCurrentUrl().contains(EXPECTED_URL_TEXT));
        perfNavigationTiming.writeToInflux("DesktopsPage");

        return this;
    }

    public DesktopsPage openFirstDesktop() {
        String firstDesktopInTheList = desktopsProductList.get(0).getText();
        desktopsProductList.get(0).click();
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        Assert.assertTrue(openedDesktopTitle.getText().contains(firstDesktopInTheList));
        waitUntilPageIsFullyLoaded(wait);
        perfNavigationTiming.writeToInflux("ProductsPage");

        return this;
    }

}
