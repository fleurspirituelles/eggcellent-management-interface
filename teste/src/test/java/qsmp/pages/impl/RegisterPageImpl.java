package qsmp.pages.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import qsmp.pages.IndexPage;
import qsmp.pages.RegisterPage;

import java.time.Duration;
import java.util.Objects;


import static qsmp.pages.impl.util.Utilities.getPagesDirectory;

public final class RegisterPageImpl implements RegisterPage {
    private final WebDriver driver;

    private RegisterPageImpl(WebDriver driver) {
        this.driver = driver;
    }

    public static RegisterPageImpl openPage(WebDriver driver) {
        Objects.requireNonNull(driver, "The driver cannot be null");

        driver.get("file:" + getPageURI());
        var page = new RegisterPageImpl(driver);
        page.waitPageLoad();

        return page;
    }

    public static  RegisterPageImpl movingTo(WebDriver driver) {
        var page = new RegisterPageImpl(driver);
        page.waitPageLoad();
        return page;
    }

    private static String getPageURI() {
        return getPagesDirectory() + "/register.html";
    }

    @Override
    public void waitPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.titleIs("QSMP Eggs Registration"));
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public void writeName(String name) {
        var nameField = driver.findElement(By.id("name"));
        nameField.sendKeys(name);
    }

    @Override
    public void writeBirthday(String birthday) {
        var birthdayField = driver.findElement(By.id("birthday"));
        birthdayField.sendKeys(birthday);
    }

    @Override
    public void selectLanguageByIndex(int index) {
        if (index < 0)
            throw new IllegalArgumentException("Index must be a non-negative number. Provided: " + index);

        var languages = driver.findElements(By.name("languages[]"));

        if (index > languages.size())
            throw new IllegalArgumentException("Index is greater than the languages quantity. Quantity: " +
                    languages.size() + ", provided: " + index);

        languages.get(index).click();
    }

    @Override
    public int getNumberOfLanguages() {
        return driver.findElements(By.name("languages[]")).size();
    }

    @Override
    public void selectParentByIndex(int index) {
        var parentElement = driver.findElement(By.id("parentSelect"));
        var parentSelect = new Select(parentElement);
        parentSelect.selectByIndex(index);
    }

    @Override
    public int getNumberOfParentOptions() {
        return driver.findElements(By.cssSelector("#parentSelect option")).size();
    }

    @Override
    public void selectSecondParentByIndex(int index) {
        var secondParentElement = driver.findElement(By.id("secondParentSelect"));
        var secondParentSelect = new Select(secondParentElement);
        secondParentSelect.selectByIndex(index);
    }

    @Override
    public int getNumberSecondParentOptions() {
        return driver.findElements(By.cssSelector("#secondParentSelect option")).size();
    }

    @Override
    public IndexPage backToIndex() {
        var backToIndexAnchor = driver.findElement(By.partialLinkText("Index"));
        backToIndexAnchor.click();
        return IndexPageImpl.movingTo(driver);
    }

    @Override
    public IndexPage registryEgg() {
        var registryButton = driver.findElement(By.xpath("//button[text()='Register']"));
        registryButton.click();
        return backToIndex();
    }

    @Override
    public void waitRegisterButtonToBeClick(Duration duration) {
        new WebDriverWait(driver, duration).until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Register']")));
    }
}
