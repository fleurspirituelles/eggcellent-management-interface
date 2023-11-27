package pages;

import org.openqa.selenium.WebDriver;

import java.util.List;

public interface EggComponent {
    String getName();

    String getBirthday();

    List<String> getLanguages();


    String getParent();

    String getSecondParent();

    void delete();

    EditPage edit(WebDriver driver);
}
