import org.testng.annotations.Test;

import static util.Constants.*;


public class RDDemo extends SetUpScenario {
    @Test(priority = 1)
    public void openMainPage() {MAIN_PAGE.openMainPage();}

    @Test(priority = 2)
    public void openComputersPage() {COMPUTERS_PAGE.openComputersPage();}

    @Test(priority = 3)
    public void openDesktopsPage() {DESKTOPS_PAGE.openDesktopsPage();}

    @Test(priority = 4)
    public void openProductsPage() {DESKTOPS_PAGE.openFirstDesktop();}

}
