import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

public class AppTest {

    @Test
    @DisplayName("should go to add new egg page")
    void shouldGoToAddNewEggPage() {
        String url = "D:\\Faculdade\\Códigos\\Visual Studio\\Site a ser testado - TC1\\eggcellent-management-interface\\msedgedriver.exe";
        String pageIndex = "D:\\Faculdade\\Códigos\\Visual Studio\\Site a ser testado - TC1\\eggcellent-management-interface\\pages\\index.html";

        System.setProperty("webdriver.edge.driver", url);

        WebDriver driver = new EdgeDriver();

        try {
            driver.get(pageIndex);

            WebElement addNewEggButton = driver.findElement(By.linkText("Add New Egg"));
            addNewEggButton.click();

        } finally {
            driver.quit();
        }
    }
}
