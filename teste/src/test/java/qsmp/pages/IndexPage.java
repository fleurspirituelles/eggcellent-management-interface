package qsmp.pages;

import java.time.Duration;

public interface IndexPage extends Page {
    int getNumberOfEggs();

    EggComponent getEggByIndex(int index);

    void waitEggsLoad(Duration duration);

    void deleteEggByIndex(int index);

    EditPage editEggByIndex(int index);

    RegisterPage goToRegisterPage();

    void deleteLast();
}
