package qsmp.pages;

import org.openqa.selenium.WebDriver;

public interface PagesFactory {
    IndexPage openIndexPage(WebDriver driver);

    RegisterPage openRegisterPage(WebDriver driver);

    EditPage openEditPage(WebDriver driver, int eggId);
}
