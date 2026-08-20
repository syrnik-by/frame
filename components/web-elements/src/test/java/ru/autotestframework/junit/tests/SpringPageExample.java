package ru.autotestframework.junit.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.pages.local.MultipleProgressBarsPage;
import ru.autotestframework.pages.local.TestFormPage;
import ru.autotestframework.ui_core.junit.BaseUITest;

@Tag("@webElemJunit")
@Execution(ExecutionMode.CONCURRENT)
class BaseUIClassTest extends BaseUITest {

    @Autowired
    FrameworkDefaultVariables defaultVariables;

    @Test
    void exampleTest() {

        var pageUrl = defaultVariables.getVariables().get("url.form");
        defaultVariables.getVariables().get("url.form");
        Selenide.open(pageUrl);

        // PageObject approach

        new TestFormPage()
                .click("Отмена")
                .checkCondition("Комментарий", Condition.exactText("Comments..."))
                .fillFormAndSend()
                .goTo(MultipleProgressBarsPage.class)
                .goTo(TestFormPage.class);
    }
}
