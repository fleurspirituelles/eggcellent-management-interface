package pages.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.EditPage;
import pages.IndexPage;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static util.Utilities.getPagesDirectory;

public final class EditPageImpl implements EditPage {
    private final WebDriver driver;

    private EditPageImpl(WebDriver driver) {
        this.driver = driver;
    }

    public static EditPageImpl openPage(WebDriver driver, int eggIndex) {
        Objects.requireNonNull(driver, "The driver cannot be null!");

        if (eggIndex <= 0)
            throw new IllegalArgumentException("The egg index must be a positive number. Provided: " + eggIndex);

        driver.get(getPageURI(eggIndex));
        var page = new EditPageImpl(driver);
        page.waitPageLoad();

        return page;
    }

    public static EditPageImpl movingPage(WebDriver driver) {
        Objects.requireNonNull(driver, "The driver cannot be null!");

        var page = new EditPageImpl(driver);
        page.waitPageLoad();

        return page;
    }

    private static String getPageURI(int eggIndex) {
        return getPagesDirectory() + "/edit.html?id=" + eggIndex;
    }

    @Override
    public void waitPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.titleIs("QSMP Eggs Editing"));
    }

    @Override
    public int getEggId() {
        var uri = driver.getCurrentUrl();
        if (!uri.contains("?id="))
            return -1;
        return Integer.parseInt(uri.split("=")[1]);
    }

    @Override
    public void writeName(String name) {
        var nameField = getNameField();
        nameField.sendKeys(name);
    }

    @Override
    public String getName() {
        return getNameField().getText();
    }

    private WebElement getNameField() {
        return driver.findElement(By.id("name"));
    }

    @Override
    public void writeBirthday(String birthday) {
        var birthdayField = getBirthdayField();
        birthdayField.sendKeys(birthday);
    }

    @Override
    public String getBirthday() {
        return getBirthdayField().getText();
    }

    private WebElement getBirthdayField() {
        return driver.findElement(By.id("birthday"));
    }

    @Override
    public void selectLanguageByIndex(int index) {
        if (index < 0) throw new IllegalArgumentException("The index must be non-negative number. Provided: " + index);

        var languages = driver.findElements(By.name("languages[]"));

        if (index > languages.size())
            throw new IllegalArgumentException("There is not a language with index " + index);

        languages.get(index).click();
    }

    @Override
    public List<String> getSelectedLanguages() {
        return driver.findElements(By.cssSelector("[name='languages[]']:checked"))
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    @Override
    public void selectParentByIndex(int index) {
        var parentSelect = new Select(getParentElement());
        parentSelect.selectByIndex(index);
    }

    @Override
    public String getParent() {
        return getParentElement().getText();
    }

    private WebElement getParentElement() {
        return driver.findElement(By.id("parentSelect"));
    }

    @Override
    public void selectSecondParentByIndex(int index) {
        var secondParentSelect = new Select(getSecondParentElement());
        secondParentSelect.selectByIndex(index);
    }

    @Override
    public String getSecondParent() {
        return getSecondParentElement().getText();
    }

    private WebElement getSecondParentElement() {
        return driver.findElement(By.id("secondParentSelect"));
    }

    @Override
    public IndexPage backToIndex() {
        var driverToIndex = driver.findElement(By.partialLinkText("Index"));
        driverToIndex.click();
        return IndexPageImpl.movingTo(driver);
    }

    @Override
    public IndexPage editEgg() {
        var editEgg = driver.findElement(By.tagName("button"));
        editEgg.click();
        return IndexPageImpl.movingTo(driver);
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }
}
