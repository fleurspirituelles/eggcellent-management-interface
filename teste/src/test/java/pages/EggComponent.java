package pages;

import java.util.List;

public interface EggComponent {
    String getName();

    String getBirthday();

    List<String> getLanguages();


    String getParent();

    String getSecondParent();

    void delete();

    EditPage edit();
}
