package util.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import util.WebDriverProvider;

public final class WebDriverProviderImpl implements WebDriverProvider {
    @Override
    public WebDriver getByBrowserName(String browserName) {
        return switch (browserName) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                yield new ChromeDriver();
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                yield new EdgeDriver();
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                yield new FirefoxDriver();
            }
            case "safari" -> {
                WebDriverManager.safaridriver().setup();
                yield new SafariDriver();
            }
            default -> throw new IllegalArgumentException("Invalid browser name. Provided: " + browserName);
        };
    }
}
