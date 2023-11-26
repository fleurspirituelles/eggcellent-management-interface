package pages;

public interface RegisterPage extends Page {
    void writeName(String name);

    void writeBirthday(String birthday);

    void selectLanguageByIndex(int index);

    void selectParentByIndex(int index);

    void selectSecondParentByIndex(int index);

    IndexPage backToIndex();

    IndexPage registryEgg();
}
