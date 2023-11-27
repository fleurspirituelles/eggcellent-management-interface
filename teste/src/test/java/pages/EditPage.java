package pages;

import java.util.List;

public interface EditPage extends Page {
    int getEggId();

    void writeName(String name);

    void clearName();

    String getName();

    void writeBirthday(String name);

    void clearBirthday();

    String getBirthday();

    void selectLanguageByIndex(int index);

    List<String> getSelectedLanguages();

    int getNumberOfLanguages();

    void selectParentByIndex(int index);

    String getParent();

    int getNumberParentOptions();

    void selectSecondParentByIndex(int index);

    String getSecondParent();

    int getNumberSecondParentOptions();

    IndexPage backToIndex();

    IndexPage editEgg();
}
