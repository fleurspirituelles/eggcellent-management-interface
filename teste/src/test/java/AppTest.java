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
import pages.impl.EditPageImpl;
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
    @Tag("RegisteringSystemTag")
    @DisplayName("When registering new eggs")
    class WhenRegisteringNewEggs {
        /*
            Classes inválidas:
                - Egg com alguma informação inválida (4 possíveis testes) 
                    - Nome nulo ou vazio (1 teste) v
                    - Data de nascimento no formato inválido (1 teste) v
                    - Nenhum checkbox selecionado (1 teste) 
                    - Nenhum parent selecionado (1 teste) 

            Teste extra:
                - Teste de repetição, ou seja, criar um novo egg repetidas vezes. A implementação atual, porém, está
                  errada.
                    - É má pŕatica haver testes com loops
                    - Usar o @RepeatedTest(numeroDeRepetições)

            - Anote todos os testes com @Tag("SystemTest")
        */

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
    @Tag("EditingSystemTest")
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
    }

    @Nested
    @Tag("DeletingSystemTest")
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