package pages.impl;

import org.openqa.selenium.WebDriver;
import pages.EditPage;
import pages.IndexPage;
import pages.PagesFactory;
import pages.RegisterPage;

public final class PagesFactoryImpl implements PagesFactory {
    @Override
    public IndexPage openIndexPage(WebDriver driver) {
        return IndexPageImpl.openPage(driver);
    }

    @Override
    public RegisterPage openRegisterPage(WebDriver driver) {
        return RegisterPageImpl.openPage(driver);
    }

    @Override
    public EditPage openEditPage(WebDriver driver, int eggId) {
        return EditPageImpl.openPage(driver, eggId);
    }
}
