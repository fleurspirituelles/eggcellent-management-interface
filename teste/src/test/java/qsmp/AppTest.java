package qsmp;

import com.github.javafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import qsmp.pages.EditPage;
import qsmp.pages.IndexPage;
import qsmp.pages.PagesFactory;
import qsmp.pages.RegisterPage;
import qsmp.pages.impl.PagesFactoryImpl;
import qsmp.util.WebDriverProvider;
import qsmp.util.WebDriverProviderImpl;

import java.time.Duration;
import java.util.List;
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

    /*
        Obeservações:
            - Além dos possíveis testes a seguir, mais podem ser pensados usando outras técnicas como a Análise do Valor
              Limite

            - Graças ao Alê, foi possível constatar que o Faker começa a bugar na geração de datas quando repetindo
              muitas vezes a operação de criar um egg. Logo, para esse método, crie eggs com o mesmo dado.

            - Além disso, o formato da data de nascimento não pode ser inferido pelo html: é formato ISO? "dd/mm/yyyy"?
              "Month dd, yyyy"? Então vamos padronizar para formato ISO: yyyy-mm-dd.

            - Testes de repetição devem ser anotados com @Tag("RepetitionTest")
    */

    @Nested
    @Tag("RegisteringSystemTests")
    @DisplayName("When registering new eggs")
    class WhenRegisteringNewEggs {
        @Test
        @Tag("SystemTest")
        @DisplayName("Should register a new egg")
        void shouldRegisterANewEgg() {
            var index = addNewRandomEgg();
            var softly = new SoftAssertions();

            softly.assertThatCode(() -> index.waitEggsLoad(Duration.ofSeconds(5))).doesNotThrowAnyException();
            softly.assertThat(index.getNumberOfEggs()).isEqualTo(1);
            softly.assertAll();
        }

        @Test
        @Tag("SystemTest")
        @DisplayName("Should not add a egg with null birthday")
        void shouldNotAddAEggWithNullBirthday() {
            var registerPage = pagesFactory.openRegisterPage(driver);
            registerPage.writeName(faker.name().fullName());
            registerPage.selectLanguageByIndex(getRandomNumberOfCheckboxes(registerPage));
            registerPage.selectParentByIndex(getRandomParent(registerPage));
            var index = registerPage.registryEgg();
            assertThat(index.getNumberOfEggs()).isEqualTo(0);
        }

        @Test
        @Tag("SystemTest")
        @DisplayName("Should not add a egg with null name")
        void shouldNotAddAEggWithNullName() {
            var registerPage = pagesFactory.openRegisterPage(driver);
            registerPage.writeBirthday(faker.date().birthday().toString());
            registerPage.selectLanguageByIndex(getRandomNumberOfCheckboxes(registerPage));
            registerPage.selectParentByIndex(getRandomParent(registerPage));
            var index = registerPage.registryEgg();
            assertThat(index.getNumberOfEggs()).isEqualTo(0);
        }

        @Test
        @Tag("SystemTest")
        @DisplayName("Should not add a egg with null checkBox")
        void shouldNotAddAEggWithNullCheckBox() {
            var registerPage = pagesFactory.openRegisterPage(driver);
            registerPage.writeName(faker.name().fullName());
            registerPage.writeBirthday(faker.date().birthday().toString());
            registerPage.selectParentByIndex(getRandomParent(registerPage));
            var index = registerPage.registryEgg();
            assertThat(index.getNumberOfEggs()).isEqualTo(0);
        }

        @RepeatedTest(1000)
        @Tag("RepetitionTest")
        @Tag("SystemTest")
        @DisplayName("Should register 1000 new egg")
        void shouldRegister1000NewEgg() {
            var index = addNewRandomEgg();
            var softly = new SoftAssertions();

            softly.assertThatCode(() -> index.waitEggsLoad(Duration.ofSeconds(5))).doesNotThrowAnyException();
            softly.assertThat(index.getNumberOfEggs()).isEqualTo(1);
            softly.assertAll();
        }

    }

    @Nested
    @Tag("EditingSystemTests")
    @DisplayName("When editing eggs")
    class WhenEditingEggs {
        /*
            Classes válidas:
                - Preenchimento correto de informações (pelo menos 5 possíveis testes)
                    - Mudança válida de nome (1 teste)
                    - Mudança válida de data de nascimento (1 teste)
                    - Mudança válida de seleção das checkboxes (1 teste)
                    - Mudança válida de fist parent (1 teste)
                    - Mudança válida de second parent (1 teste)

            Classes inválidas:
                - Preenchimento inválido de informações (pelo menos 5 possíveis testes):
                    - Mudança para nome inválido (1 teste)
                    - Mudança para data de nascimento com formato inválido (1 teste)
                    - Mudança para nenhuma checkbox selecionada (1 teste)
                    - Mudança para parent inválido (1 teste)
                    - Mudança para second parent inválido (1 teste)

            - Anote todos os métodos de teste com @Tag("SystemTest")
        */

        @Test
        @Tag("SystemTest")
        @DisplayName("Should edit name an egg")
        void shouldEditNameAnEgg() {
            addNewRandomEgg();
            var indexPage = pagesFactory.openIndexPage(driver);
            var editPage = indexPage.editEggByIndex(0);

            editName(editPage);

            var index = editPage.editEgg();
            assertThat(index.getEggByIndex(0).getName()).isNotEqualTo(faker.name().fullName());
        }

        @Test
        @Tag("SystemTest")
        @DisplayName("Should edit birthday an egg")
        void shouldEditBirthdayAnEgg() {
            addNewRandomEgg();
            var indexPage = pagesFactory.openIndexPage(driver);
            var editPage = indexPage.editEggByIndex(0);

            editBirthday(editPage);

            var index = editPage.editEgg();
            assertThat(index.getEggByIndex(0).getBirthday()).isNotEqualTo(faker.date().birthday().toString());
        }

        @Test
        @Tag("SystemTest")
        @DisplayName("Should edit languages an egg")
        void shouldEditLanguagesAnEgg() {
            addNewRandomEgg();
            var indexPage = pagesFactory.openIndexPage(driver);
            var editPage = indexPage.editEggByIndex(0);

            List<String> languages = editPage.getSelectedLanguages();

            ediCheckBox(editPage);

            var index = editPage.editEgg();
            assertThat(index.getEggByIndex(0).getLanguages()).isNotEqualTo(languages);
        }

        @Test
        @Tag("SystemTest")
        @DisplayName("Should edit parent an egg")
        void shouldEditParentAnEgg() {
            addNewRandomEgg();
            var indexPage = pagesFactory.openIndexPage(driver);
            var editPage = indexPage.editEggByIndex(0);

            String parent = editPage.getParent();

            editParent(editPage);

            var index = editPage.editEgg();
            assertThat(index.getEggByIndex(0).getParent()).isNotEqualTo(parent);
        }

        @Test
        @Tag("SystemTest")
        @DisplayName("Should edit second parent an egg")
        void shouldEditSecondParentAnEgg() {
            addNewRandomEgg();
            var indexPage = pagesFactory.openIndexPage(driver);
            var editPage = indexPage.editEggByIndex(0);

            String secondParent = editPage.getSecondParent();

            editPage.selectSecondParentByIndex((int) floor(Math.random() * editPage.getNumberSecondParentOptions()));

            var index = editPage.editEgg();
            assertThat(index.getEggByIndex(0).getSecondParent()).isNotEqualTo(secondParent);
        }
    }

    @Nested
    @Tag("DeletingSystemTests")
    @DisplayName("When deleting eggs")
    class WhenDeletingEggs {
        /*
            Classes válidas:
                - Egg existente (1 teste)

            Classes inválidas:
                - Egg inexistente

            - Problema com esse: o site foi desenvolvido de uma forma que não dá para testar uma classe inválida para
              isso

            - Anote todos os testes com @Tag("SystemTest")
        */

        @Test
        @Tag("SystemTest")
        @DisplayName("Should delete a registered egg")
        void shouldDeleteARegisteredEgg() {
            var index = addNewRandomEgg();
            index.deleteLast();
            assertThat(index.getNumberOfEggs()).isEqualTo(0);
        }
    }

    private record Elements(InitialElement initial, EditedElement edited) {
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

        register.waitRegisterButtonToBeClick(Duration.ofSeconds(10));

        return register.registryEgg();
    }

    private void selectCheckboxes(int numberOfCheckboxes, RegisterPage register) {
        for (int i = 0; i < numberOfCheckboxes; i++) {
            register.selectLanguageByIndex(i);
        }
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

    private static void editName(EditPage edit) {
        Faker faker = new Faker();

        edit.clearName();

        edit.writeName(faker.name().fullName());
    }

    private static void editBirthday(EditPage edit) {
        Faker faker = new Faker();

        edit.clearBirthday();

        var fakeBirthday = faker.date().birthday();
        edit.writeBirthday(fakeBirthday.toString());
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

    private static void editParent(EditPage edit) {
        edit.selectParentByIndex((int) floor(Math.random() * edit.getNumberParentOptions()));
    }

    private static void editParents(EditPage edit) {
        edit.selectParentByIndex((int) floor(Math.random() * edit.getNumberParentOptions()));
        edit.selectSecondParentByIndex((int) floor(Math.random() * edit.getNumberSecondParentOptions()));
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