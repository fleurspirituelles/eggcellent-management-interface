package pages;

import java.util.List;

public interface EditPage extends Page {
    int getEggId();

    void writeName(String name);

    String getName();

    void writeBirthday(String name);

    String getBirthday();

    void selectLanguageByIndex(int index);

    List<String> getSelectedLanguages();

    void selectParentByIndex(int index);

    String getParent();

    void selectSecondParentByIndex(int index);

    String getSecondParent();

    IndexPage backToIndex();

    IndexPage editEgg();
}
