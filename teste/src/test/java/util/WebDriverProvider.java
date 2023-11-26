package util;

import org.openqa.selenium.WebDriver;

public interface WebDriverProvider {
    WebDriver getByBrowserName(String browserName);
}
