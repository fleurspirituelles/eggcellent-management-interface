package pages.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import util.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static util.Utilities.getPagesDirectory;

public final class IndexPageImpl implements IndexPage {
    private final WebDriver driver;
    private final List<EggComponentImpl> eggs;

    private IndexPageImpl(WebDriver driver, List<EggComponentImpl> eggs) {
        this.driver = driver;
        this.eggs = eggs;
    }

    public static IndexPage openPage(WebDriver driver) {
        Objects.requireNonNull(driver, "The web driver cannot be null!");

        driver.get("file:"+getPageURI());
        var eggs = findEggs(driver);

        return new IndexPageImpl(driver, eggs);
    }

    public static IndexPage movingTo(WebDriver driver) {
        var eggs = findEggs(driver);
        return new IndexPageImpl(driver, eggs);
    }

    private static String getPageURI() {
        return getPagesDirectory() + "/index.html";
    }

    private static List<EggComponentImpl> findEggs(WebDriver driver) {
        wainPage(driver);
        return driver.findElements(By.tagName("tr"))
                .stream()
                .map(EggComponentImpl::new)
                .toList();
    }

    private static void wainPage(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("tableBody")));
    }

    @Override
    public void waitPageLoad() {
        wainPage(driver);
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
    public void deleteLast() {
        deleteEggByIndex(eggs.size() - 1);
    }

    @Override
    public EditPage editEggByIndex(int index) {
        return getEggByIndex(index).edit(driver);
    }

    @Override
    public RegisterPage goToRegisterPage() {
        var registerPageAnchor = driver.findElement(By.linkText("Add New Egg"));
        registerPageAnchor.click();
        return RegisterPageImpl.movingTo(driver);
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }
}
