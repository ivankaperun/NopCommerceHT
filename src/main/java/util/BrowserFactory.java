package util;

import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;


public class BrowserFactory {
    @SneakyThrows
    public WebDriver startBrowser(String browserName, WebDriver driver) {
//        if ("Firefox".equalsIgnoreCase(browserName)) {
//            driver = new FirefoxDriver();
//        } else if ("ChromeLocal".equalsIgnoreCase(browserName)) {
//            driver = new ChromeDriver();
//        }

        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));
        return driver;
    }
}
