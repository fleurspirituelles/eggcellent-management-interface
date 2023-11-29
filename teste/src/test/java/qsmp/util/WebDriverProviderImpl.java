package qsmp.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.IOException;
import java.util.Properties;

public final class WebDriverProviderImpl implements WebDriverProvider {
    private static final String PROPERTIES_FILE = "tests.config.properties";

    @Override
    public WebDriver getWebDriver() {
        var browserName = getPropertiesResource().getProperty("driver.browser.name");

        if (browserName == null) throw new MissingConfigurationException(
            "It is missing driver.browser.name property which should describe the browser name to find the proper driver"
        );

        return getByBrowserName(browserName);
    }

    private Properties getPropertiesResource() {
        var properties = new Properties();
        var loader = WebDriverProviderImpl.class.getClassLoader();

        try (var stream = loader.getResourceAsStream(PROPERTIES_FILE)) {
            if (stream == null) throw new MissingConfigurationException(
                "It is missing " + PROPERTIES_FILE + " detailing required configurations to find the proper web " +
                    "driver! Please, create file: src/test/resources/" + PROPERTIES_FILE
            );
            properties.load(stream);
        }
        catch (IOException e) {
            throw new MissingConfigurationException(e.getMessage());
        }

        return properties;
    }

    private WebDriver getByBrowserName(String browserName) {
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
            default -> throw new IllegalArgumentException(
                "Invalid browser name: " + browserName + ". Accepted browser by now: 'chrome', 'edge', 'firefox', " +
                    "'safari'"
            );
        };
    }
}
