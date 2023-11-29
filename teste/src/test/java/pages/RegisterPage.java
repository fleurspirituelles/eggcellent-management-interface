package pages;

import java.time.Duration;

public interface RegisterPage extends Page {
    void writeName(String name);

    void writeBirthday(String birthday);

    int getNumberOfLanguages();

    void selectLanguageByIndex(int index);

    void selectParentByIndex(int index);

    int getNumberOfParentOptions();

    void selectSecondParentByIndex(int index);

    int getNumberSecondParentOptions();

    IndexPage backToIndex();

    IndexPage registryEgg();

    void waitRegisterButtonToBeClick(Duration duration);
}
