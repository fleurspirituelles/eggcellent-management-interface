import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AppTest {
    String url = "D:\\Faculdade\\Códigos\\Visual Studio\\Site a ser testado - TC1\\eggcellent-management-interface\\msedgedriver.exe";
    String pageIndex = "D:\\Faculdade\\Códigos\\Visual Studio\\Site a ser testado - TC1\\eggcellent-management-interface\\pages\\index.html";
    WebDriver driver = new EdgeDriver();


    @Test
    @DisplayName("should register  a new egg")
    void shouldRegisterANewEgg() {
        addNewEgg();
    }

    @Test
    @DisplayName("Should delete a registered egg")
    void shouldDeleteARegisteredEgg() throws InterruptedException{
        addNewEgg();

        Thread.sleep(1000);

        WebElement delete = driver.findElement(By.xpath("//button[text()='Delete']"));
        delete.click();
    }


    private void addNewEgg() {
        System.setProperty("webdriver.edge.driver", url);
        driver.get(pageIndex);

        WebElement addNewEggButton = driver.findElement(By.linkText("Add New Egg"));
        addNewEggButton.click();

        WebElement name = driver.findElement(By.id("name"));
        WebElement birthday = driver.findElement(By.id("birthday"));
        WebElement firstParent = driver.findElement(By.id("parentSelect"));
        WebElement secondParent = driver.findElement(By.id("secondParentSelect"));
        List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@type='checkbox']"));

        Faker faker = new Faker();

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

        WebElement registerButton = driver.findElement(By.xpath("//button[text()='Register']"));
        registerButton.click();

        WebElement returnIndex = driver.findElement(By.linkText("Back to Index"));
        returnIndex.click();
    }
}