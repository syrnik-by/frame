package ru.autotestframework.junit.tests;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.impl.CoreSelenidePageFactory;
import io.qameta.allure.Step;
import ru.autotestframework.ui_core.junit.InjectedPage;

public abstract class ProjectAbstractPage<T> extends InjectedPage<T> {

    @Step("Инициализировать страницу {pageName}")
    public <B extends InjectedPage> B goTo(Class<B> pageClass) {
        return new CoreSelenidePageFactory().page(WebDriverRunner.driver(), pageClass);
    }
}
