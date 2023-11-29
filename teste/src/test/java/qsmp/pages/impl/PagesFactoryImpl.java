package qsmp.pages.impl;

import org.openqa.selenium.WebDriver;
import qsmp.pages.EditPage;
import qsmp.pages.IndexPage;
import qsmp.pages.PagesFactory;
import qsmp.pages.RegisterPage;

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
