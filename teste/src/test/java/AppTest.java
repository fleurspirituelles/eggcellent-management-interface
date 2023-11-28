import com.github.javafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.EditPage;
import pages.IndexPage;
import pages.PagesFactory;
import pages.RegisterPage;
import pages.impl.PagesFactoryImpl;
import util.WebDriverProvider;
import util.WebDriverProviderImpl;

import java.time.Duration;
import java.util.Random;

import static java.lang.Math.floor;
import static java.lang.Math.min;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    private final WebDriverProvider driverProvider = new WebDriverProviderImpl();
    private final PagesFactory pagesFactory = new PagesFactoryImpl();
    private Faker faker;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = driverProvider.getWebDriver();
        faker = new Faker();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Should register a new egg")
    void shouldRegisterANewEgg() {
        var index = addNewRandomEgg();
        var softly = new SoftAssertions();

        softly.assertThatCode(() -> index.waitEggsLoad(Duration.ofSeconds(5))).doesNotThrowAnyException();
        softly.assertThat(index.getNumberOfEggs()).isEqualTo(1);
        softly.assertAll();
    }

    @Test
    @Disabled
    @DisplayName("Should register 1000 eggs in sequence")
    void shouldRegister1000EggsInSequence() {
        add1000Eggs();
        int visibleTrCount = countTrElement();
        assertThat(visibleTrCount).isEqualTo(1000);
    }

    @Test
    @DisplayName("Should delete a registered egg")
    void shouldDeleteARegisteredEgg(){
        var index = addNewRandomEgg();
        index.deleteLast();
        assertThat(index.getNumberOfEggs()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should edit a registered egg")
    void shouldEditARegisteredEgg(){
        var index = addNewRandomEgg();
        Elements element = getElements(index);
        assertThatElement(element);
    }

    private record Elements(InitialElement initial, EditedElement edited) {
    }

    @Nested
    @DisplayName("Test of blank spaces")
    class TestBlankSpace{
        @Test
        @DisplayName("Should return the name as a blank space")
        void shouldReturnABlankSpace(){
            addNewRandomEgg();
            WebElement belowName = editElementToABlankSpace("name");
            assertThat(belowName.getText()).isBlank();
        }

        @Test
        @DisplayName("Should return the birthday as a blank space")
        void ShouldReturnTheBirthdayAsABlankSpace() {
            addNewRandomEgg();
            WebElement belowBirthday = editElementToABlankSpace("birthday");
            assertThat(belowBirthday.getText()).isBlank();
        }
    }

    private IndexPage addNewRandomEgg() {
        var register = pagesFactory.openRegisterPage(driver);

        var name = faker.name().fullName();
        var birthday = faker.date().birthday().toString();
        var numberOfCheckboxes = getRandomNumberOfCheckboxes(register);
        var parentIndex = getRandomParent(register);
        var secondParentIndex = getRandomSecondParent(register);

        return registryEgg(register, name, birthday, numberOfCheckboxes, parentIndex, secondParentIndex);
    }

    private int getRandomNumberOfCheckboxes(RegisterPage register) {
        Random random = new Random()
                ;
        int minimumCheckboxes = 1;
        int numberOfLanguages = register.getNumberOfLanguages();
        int checkBoxSelected = random.nextInt(numberOfLanguages) + minimumCheckboxes;

        return min(checkBoxSelected, numberOfLanguages);
    }

    private int getRandomParent(RegisterPage register) {
        return (int) floor(Math.random() * register.getNumberOfParentOptions());
    }

    private int getRandomSecondParent(RegisterPage register) {
        return (int) floor(Math.random() * register.getNumberSecondParentOptions());
    }

    private IndexPage registryEgg(RegisterPage register, String name, String birthday, int numberOfCheckboxes,
                                  int parentIndex, int secondParentIndex) {
        register.writeName(name);
        register.writeBirthday(birthday);
        selectCheckboxes(numberOfCheckboxes, register);
        register.selectParentByIndex(parentIndex);
        register.selectSecondParentByIndex(secondParentIndex);

        return register.registryEgg();
    }

    private void selectCheckboxes(int numberOfCheckboxes, RegisterPage register) {
        for (int i = 0; i < numberOfCheckboxes; i++) {
            register.selectLanguageByIndex(i);
        }
    }

    private void add1000Eggs() {
        for (int i = 0; i < 1000; i++) {
            addNewRandomEgg();
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

        for (int i = 0; i < min(checkBoxSelected, numberOfLanguages); i++) {
            edit.selectLanguageByIndex(i);
        }
    }

    private static void editParents(EditPage edit) {
        edit.selectParentByIndex((int) floor(Math.random() * edit.getNumberParentOptions()));
        edit.selectSecondParentByIndex((int) floor(Math.random() * edit.getNumberSecondParentOptions()));
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

    private Elements getElements(IndexPage index) {
        InitialElement initial = getInitialElement();
        index = editEgg(index);
        EditedElement edited = getEditedElement();
        return new Elements(initial, edited);
    }

    private record InitialElement(WebElement name, WebElement birthday, WebElement firtsParent) {}

    private InitialElement getInitialElement() {
        WebElement name = driver.findElement(By.xpath("(//tbody/tr/td)[1]"));
        WebElement birthday = driver.findElement(By.xpath("(//tbody/tr/td)[2]"));
        WebElement firtsParent = driver.findElement(By.xpath("(//tbody/tr/td)[4]"));
        return new InitialElement(name, birthday, firtsParent);
    }

    private record EditedElement(WebElement nameEdited, WebElement birthdayEdited, WebElement firtsParentEdited) {}

    private EditedElement getEditedElement() {
        WebElement nameEdited = driver.findElement(By.xpath("(//tbody/tr/td)[1]"));
        WebElement birthdayEdited = driver.findElement(By.xpath("(//tbody/tr/td)[2]"));
        WebElement firtsParentEdited = driver.findElement(By.xpath("(//tbody/tr/td)[4]"));
        return new EditedElement(nameEdited, birthdayEdited, firtsParentEdited);
    }

    private static void assertThatElement(Elements element) {
        assertThat(element.initial().name()).isNotEqualTo(element.edited().nameEdited());
        assertThat(element.initial().birthday()).isNotEqualTo(element.edited().birthdayEdited());
        assertThat(element.initial().firtsParent()).isNotEqualTo(element.edited().firtsParentEdited());
    }
}