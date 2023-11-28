package pages.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.EditPage;
import pages.EggComponent;
import pages.IndexPage;
import pages.RegisterPage;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static util.Utilities.getPagesDirectory;

public final class IndexPageImpl implements IndexPage {
    private final WebDriver driver;

    private IndexPageImpl(WebDriver driver) {
        this.driver = driver;
    }

    public static IndexPage openPage(WebDriver driver) {
        Objects.requireNonNull(driver, "The web driver cannot be null!");

        driver.get("file:"+getPageURI());
        var index = new IndexPageImpl(driver);
        index.waitPageLoad();

        return index;
    }

    public static IndexPage movingTo(WebDriver driver) {
        var index = new IndexPageImpl(driver);
        index.waitPageLoad();
        return index;
    }

    private static String getPageURI() {
        return getPagesDirectory() + "/index.html";
    }

    @Override
    public void waitPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("tableBody")));
    }

    @Override
    public int getNumberOfEggs() {
        if (!getEggsElements().isEmpty()){
            return getEggsElements().size();
        }else{
            return 0;
        }
    }

    @Override
    public EggComponent getEggByIndex(int index) {
        if (index < 0)
            throw new IllegalArgumentException("The index must be a non-negative number! Provided: " + index);
        return getEggsElements().stream()
                .map(EggComponentImpl::new)
                .toList()
                .get(index);
    }

    private List<WebElement> getEggsElements() {
        return driver.findElements(By.cssSelector("#tableBody tr"));
    }

    @Override
    public void deleteEggByIndex(int index) {
        getEggByIndex(index).delete();
    }

    @Override
    public void deleteLast() {
        deleteEggByIndex(getNumberOfEggs() - 1);
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
