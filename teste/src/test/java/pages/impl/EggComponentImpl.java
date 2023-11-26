package pages.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.EditPage;
import pages.EggComponent;

import java.util.Arrays;
import java.util.List;

public final class EggComponentImpl implements EggComponent {
    private final WebElement eggElement;

    public EggComponentImpl(WebElement eggElement) {
        this.eggElement = eggElement;
    }

    @Override
    public String getName() {
        return eggElement.findElement(By.cssSelector(":first-child"))
                .getText();
    }

    @Override
    public String getBirthday() {
        return eggElement.findElement(By.cssSelector(":nth-child(2)"))
                .getText();
    }

    @Override
    public List<String> getLanguages() {
        return Arrays.stream(
            eggElement.findElement(By.cssSelector(":nth-child(3)"))
            .getText()
            .split(", ")
        ).toList();
    }

    @Override
    public String getParent() {
        return eggElement.findElement(By.cssSelector(":nth-child(4)"))
                .getText();
    }

    @Override
    public String getSecondParent() {
        return eggElement.findElement(By.cssSelector(":nth-child(5)"))
                .getText();
    }

    @Override
    public void delete() {
        eggElement.findElement(By.id("deleteButton")).click();
    }

    @Override
    public EditPage edit() {
        return null;
    }
}
