import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.IndexPage;
import pages.impl.IndexPageImpl;
import pages.impl.RegisterPageImpl;
import util.WebDriverProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AppTest {
    public static final String BROWSER_NAME = "chrome";
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
        addNewEgg();

        Thread.sleep(1000);

        editEgg();
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

        Faker faker = new Faker();
        register.writeName(faker.name().fullName());

        var fakeBirthday = faker.date().birthday();
        register.writeBirthday(fakeBirthday.toString());

        Random random = new Random();
        int minimumCheckboxes = 1;
        int numberOfLanguages = register.getNumberOfLanguages();
        int checkBoxSelected = random.nextInt(numberOfLanguages) + minimumCheckboxes;
        for (int i = 0; i < Math.min(checkBoxSelected, numberOfLanguages); i++) {
            register.selectLanguageByIndex(i);
        }

        register.selectParentByIndex((int) Math.floor(Math.random() * register.getNumberOfParentOptions()));
        register.selectSecondParentByIndex((int) Math.floor(Math.random() * register.getNumberSecondParentOptions()));

        return register.registryEgg();
    }
    
    private void editEgg() {
        WebElement edit = driver.findElement(By.xpath("//button[text()='Edit']"));
        edit.click();

        WebElement name = driver.findElement(By.id("name"));
        WebElement birthday = driver.findElement(By.id("birthday"));
        WebElement firstParent = driver.findElement(By.id("parentSelect"));
        WebElement secondParent = driver.findElement(By.id("secondParentSelect"));
        List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@type='checkbox']"));

        Faker faker = new Faker();

        name.clear();
        birthday.clear();

        name.sendKeys(faker.name().fullName());

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        birthday.sendKeys(simpleDateFormat.format(faker.date().past(5, TimeUnit.DAYS)));

        Random random = new Random();
        int minimumCheckBoxes = 1;
        int checkBoxSelected = random.nextInt(checkboxes.size()) + minimumCheckBoxes;
        for (int i = 0; i < Math.min(checkBoxSelected, checkboxes.size()); i++) {
            checkboxes.get(i).click();
        }

        List<String> firstParentOptions = firstParent.findElements(By.tagName("option")).stream().map(option -> option.getAttribute("value")).toList();
        firstParent.sendKeys(firstParentOptions.get((int) Math.floor(Math.random() * firstParentOptions.size())));

        List<String> secondParentOptions = secondParent.findElements(By.tagName("option")).stream().map(option -> option.getAttribute("value")).toList();
        secondParent.sendKeys(secondParentOptions.get((int) Math.floor(Math.random() * secondParentOptions.size())));

        WebElement editButton = driver.findElement(By.xpath("//button[text()='Edit']"));
        editButton.click();

        WebElement returnIndex = driver.findElement(By.linkText("Back to Index"));
        returnIndex.click();
    }
}