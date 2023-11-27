import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.EditPage;
import pages.IndexPage;
import pages.impl.RegisterPageImpl;
import util.WebDriverProvider;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    public static final String BROWSER_NAME = "edge";
    String pageIndex = "D:\\Faculdade\\Códigos\\Visual Studio\\Site a ser testado - TC1\\eggcellent-management-interface\\pages\\index.html";
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = WebDriverProvider.getByBrowserName(BROWSER_NAME);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("should register  a new egg")
    void shouldRegisterANewEgg() {
        addNewEgg();
    }

    @Test
    @DisplayName("Should delete a registered egg")
    void shouldDeleteARegisteredEgg() throws InterruptedException{
        var index = addNewEgg();
        index.deleteLast();
        // TODO: where is the assertion?
    }

    @Test
    @DisplayName("Should edit a registered egg")
    void shouldEditARegisteredEgg() throws InterruptedException{
        var index = addNewEgg();
        index = editEgg(index);
        // TODO: where is the assertion?
    }


    @Nested
    @DisplayName("Test of blank spaces")
    class TestBlankSpace{
        @Test
        @DisplayName("Should return the name as a blank space")
        void shouldReturnABlankSpace(){
            addNewEgg();

            WebElement belowName = editElementToABlankSpace("name");

            assertThat(belowName.getText()).isBlank();
        }

        @Test
        @DisplayName("Should return the birthday as a blank space")
        void ShouldReturnTheBirthdayAsABlankSpace() {
            addNewEgg();

            WebElement belowBirthday = editElementToABlankSpace("birthday");

            assertThat(belowBirthday.getText()).isBlank();
        }
    }

    private WebElement editElementToABlankSpace(String element) {
        WebElement edit = driver.findElement(By.xpath("//button[text()='Edit']"));
        edit.click();

        WebElement webElement = driver.findElement(By.id(element));
        webElement.clear();

        WebElement editButton = driver.findElement(By.xpath("//button[text()='Edit']"));
        editButton.click();

        WebElement returnIndex = driver.findElement(By.linkText("Back to Index"));
        returnIndex.click();

        if (element.equals("name")){
            return driver.findElement(By.xpath("(//tbody/tr/td)[1]"));
        }
        return driver.findElement(By.xpath("(//tbody/tr/td)[2]"));
    }

    private IndexPage addNewEgg() {
        var register = RegisterPageImpl.openPage(driver);

        fillAllField(register);

        return register.registryEgg();
    }

    private static void fillAllField(RegisterPageImpl register) {
        fakerNameAndBirthday(register);

        randomCheckBox(register);

        randomParent(register);
    }

    private static void fakerNameAndBirthday(RegisterPageImpl register) {
        Faker faker = new Faker();
        register.writeName(faker.name().fullName());

        var fakeBirthday = faker.date().birthday();
        register.writeBirthday(fakeBirthday.toString());
    }

    private static void randomCheckBox(RegisterPageImpl register) {
        Random random = new Random();
        int minimumCheckboxes = 1;
        int numberOfLanguages = register.getNumberOfLanguages();
        int checkBoxSelected = random.nextInt(numberOfLanguages) + minimumCheckboxes;
        for (int i = 0; i < Math.min(checkBoxSelected, numberOfLanguages); i++) {
            register.selectLanguageByIndex(i);
        }
    }

    private static void randomParent(RegisterPageImpl register) {
        register.selectParentByIndex((int) Math.floor(Math.random() * register.getNumberOfParentOptions()));
        register.selectSecondParentByIndex((int) Math.floor(Math.random() * register.getNumberSecondParentOptions()));
    }

    private IndexPage editEgg(IndexPage index) {
        var edit = index.editEggByIndex(0);

        editNameAndBirthday(edit);

        Random random = new Random();
        int minimumCheckBoxes = 1;
        int numberOfLanguages = edit.getNumberOfLanguages();
        int checkBoxSelected = random.nextInt(numberOfLanguages) + minimumCheckBoxes;
        for (int i = 0; i < Math.min(checkBoxSelected, numberOfLanguages); i++) {
            edit.selectLanguageByIndex(i);
        }

        edit.selectParentByIndex((int) Math.floor(Math.random() * edit.getNumberParentOptions()));
        edit.selectSecondParentByIndex((int) Math.floor(Math.random() * edit.getNumberSecondParentOptions()));

        return edit.editEgg();
    }

    private static void editNameAndBirthday(EditPage edit) {
        Faker faker = new Faker();

        edit.clearName();
        edit.clearBirthday();

        edit.writeName(faker.name().fullName());
        var fakeBirthday = faker.date().birthday();
        edit.writeBirthday(fakeBirthday.toString());
    }
}