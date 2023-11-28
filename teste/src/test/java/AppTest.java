import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.EditPage;
import pages.IndexPage;
import pages.impl.RegisterPageImpl;
import util.WebDriverProvider;
import util.WebDriverProviderImpl;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    private static final WebDriverProvider driverProvider = new WebDriverProviderImpl();
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = driverProvider.getWebDriver();
    }

    @AfterEach
    void tearDown() {driver.quit();}

    @Test
    @DisplayName("should register  a new egg")
    void shouldRegisterANewEgg() {
        addNewEgg();
        int visibleTrCount = countTrElement();
        assertThat(visibleTrCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Should register 1000 eggs in sequence")
    void shouldRegister1000EggsInSequence() {
        add1000Eggs();
        int visibleTrCount = countTrElement();
        assertThat(visibleTrCount).isEqualTo(1000);
    }

    @Test
    @DisplayName("Should delete a registered egg")
    void shouldDeleteARegisteredEgg(){
        var index = addNewEgg();
        index.deleteLast();
        assertThat(index.getNumberOfEggs()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should edit a registered egg")
    void shouldEditARegisteredEgg(){
        var index = addNewEgg();
        elements element = getElements(index);
        assertThatElement(element);
    }

    private record elements(initialElement initial, editedElement edited) {
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

    private IndexPage addNewEgg() {
        var register = RegisterPageImpl.openPage(driver);

        fillAllField(register);

        return register.registryEgg();
    }

    private void add1000Eggs() {
        for (int i = 0; i < 1000; i++) {
            addNewEgg();
        }
    }

    private int countTrElement() {
        java.util.List<WebElement> trElements = driver.findElements(By.tagName("tr"));
        int visibleTrCount = 0;
        for (WebElement trElement : trElements) {
            if (trElement.isDisplayed()) {
                visibleTrCount++;
            }
        }
        return visibleTrCount-1;
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

        fullFillTheEditFields(edit);

        return edit.editEgg();
    }

    private static void fullFillTheEditFields(EditPage edit) {
        editNameAndBirthday(edit);

        ediCheckBox(edit);

        editParents(edit);
    }

    private static void editNameAndBirthday(EditPage edit) {
        Faker faker = new Faker();

        edit.clearName();
        edit.clearBirthday();

        edit.writeName(faker.name().fullName());
        var fakeBirthday = faker.date().birthday();
        edit.writeBirthday(fakeBirthday.toString());
    }

    private static void ediCheckBox(EditPage edit) {
        Random random = new Random();
        int minimumCheckBoxes = 1;
        int numberOfLanguages = edit.getNumberOfLanguages();
        int checkBoxSelected = random.nextInt(numberOfLanguages) + minimumCheckBoxes;
        for (int i = 0; i < Math.min(checkBoxSelected, numberOfLanguages); i++) {
            edit.selectLanguageByIndex(i);
        }
    }

    private static void editParents(EditPage edit) {
        edit.selectParentByIndex((int) Math.floor(Math.random() * edit.getNumberParentOptions()));
        edit.selectSecondParentByIndex((int) Math.floor(Math.random() * edit.getNumberSecondParentOptions()));
    }



    private WebElement editElementToABlankSpace(String element) {
        return elementToLetBlank(element);
    }

    private WebElement elementToLetBlank(String element) {
        eraseElement(element);

        return elementToCheck(element);
    }

    private void eraseElement(String element) {
        WebElement edit = driver.findElement(By.xpath("//button[text()='Edit']"));
        edit.click();

        WebElement webElement = driver.findElement(By.id(element));
        webElement.clear();

        WebElement editButton = driver.findElement(By.xpath("//button[text()='Edit']"));
        editButton.click();

        WebElement returnIndex = driver.findElement(By.linkText("Back to Index"));
        returnIndex.click();
    }

    private WebElement elementToCheck(String element) {
        if (element.equals("name")){
            return driver.findElement(By.xpath("(//tbody/tr/td)[1]"));
        }
        return driver.findElement(By.xpath("(//tbody/tr/td)[2]"));
    }


    private elements getElements(IndexPage index) {
        initialElement initial = getInitialElement();

        index = editEgg(index);

        editedElement edited = getEditedElement();
        return new elements(initial, edited);
    }
    private record initialElement(WebElement name, WebElement birthday, WebElement firtsParent) {}
    private initialElement getInitialElement() {
        WebElement name = driver.findElement(By.xpath("(//tbody/tr/td)[1]"));
        WebElement birthday = driver.findElement(By.xpath("(//tbody/tr/td)[2]"));
        WebElement firtsParent = driver.findElement(By.xpath("(//tbody/tr/td)[4]"));
        return new initialElement(name, birthday, firtsParent);
    }

    private record editedElement(WebElement nameEdited, WebElement birthdayEdited, WebElement firtsParentEdited) {}
    private editedElement getEditedElement() {
        WebElement nameEdited = driver.findElement(By.xpath("(//tbody/tr/td)[1]"));
        WebElement birthdayEdited = driver.findElement(By.xpath("(//tbody/tr/td)[2]"));
        WebElement firtsParentEdited = driver.findElement(By.xpath("(//tbody/tr/td)[4]"));
        return new editedElement(nameEdited, birthdayEdited, firtsParentEdited);
    }
    private static void assertThatElement(elements element) {
        assertThat(element.initial().name()).isNotEqualTo(element.edited().nameEdited());
        assertThat(element.initial().birthday()).isNotEqualTo(element.edited().birthdayEdited());
        assertThat(element.initial().firtsParent()).isNotEqualTo(element.edited().firtsParentEdited());
    }
}