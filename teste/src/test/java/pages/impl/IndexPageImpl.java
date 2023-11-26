package pages.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public final class IndexPageImpl implements IndexPage {
    private final WebDriver driver;
    private final List<EggComponentImpl> eggs;

    private IndexPageImpl(WebDriver driver, List<EggComponentImpl> eggs) {
        this.driver = driver;
        this.eggs = eggs;
    }

    public static IndexPage openPage(WebDriver driver) {
        Objects.requireNonNull(driver, "The web driver cannot be null!");

        driver.get(getPageURI());
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.titleIs("QSMP Eggs Showcase"));
        var eggs = findEggs(driver);

        return new IndexPageImpl(driver, eggs);
    }

    private static String getPageURI() {
        var properties = new Properties();

        try (var stream = new FileInputStream("./pages-config.properties")) {
            properties.load(stream);
        }
        catch (IOException error) {
            throw new MissingConfigurationException("It is missing pages-config file detailing pages.directory.name " +
                    "prop which is intended to be use to facilitate tests configuration.");
        }

        var pagesDirectory = properties.getProperty("pages.directory.name");
        if (pagesDirectory == null)
            throw new MissingConfigurationException("It is missing pages.directory.name property detailing the " +
                    "absolute path to the pages folder");

        return pagesDirectory +  "index.html";
    }

    private static List<EggComponentImpl> findEggs(WebDriver driver) {
        return driver.findElements(By.tagName("tr"))
                .stream()
                .map(EggComponentImpl::new)
                .toList();
    }

    @Override
    public int getNumberOfEggs() {
        return eggs.size();
    }

    @Override
    public EggComponent getEggByIndex(int index) {
        if (index < 0)
            throw new IllegalArgumentException("The index must be a non-negative number! Provided: " + index);
        return eggs.get(index);
    }

    @Override
    public void deleteEggByIndex(int index) {
        getEggByIndex(index).delete();
    }

    @Override
    public EditPage editEggByIndex(int index) {
        return getEggByIndex(index).edit();
    }

    @Override
    public RegisterPage goToRegisterPage() {
        return null;
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }
}
