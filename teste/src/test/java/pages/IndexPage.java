package pages;

public interface IndexPage extends Page {
    int getNumberOfEggs();

    EggComponent getEggByIndex(int index);

    void deleteEggByIndex(int index);

    EditPage editEggByIndex(int index);

    RegisterPage goToRegisterPage();
}
