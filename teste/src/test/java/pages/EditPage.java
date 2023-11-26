package pages;

public interface EditPage extends Page {
    int getEggId();

    void writeName(String name);

    void writeBirthday(String name);

    void selectLanguageByValue(String language);

    void selectLanguageByIndex(int index);

    void selectParentByValue(String parent);

    void selectParentByIndex(int index);

    void selectSecondParentByValue(String secondParent);

    void selectSecondParentByIndex(int index);

    IndexPage backToIndex();

    IndexPage editEgg();
}
