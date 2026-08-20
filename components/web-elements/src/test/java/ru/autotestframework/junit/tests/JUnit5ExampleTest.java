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
import ru.autotestframework.pages.local.NoFrameWorkElementsPage;
import ru.autotestframework.pages.local.TestFormPage;

@Tag("@webElemJunit")
@Execution(ExecutionMode.CONCURRENT)
class JUnit5ExampleTest extends JUnitUIBaseExampleTest {

    @Autowired
    FrameworkDefaultVariables defaultVariables;

    @Test
    void exampleTest() {

        var pageUrl = defaultVariables.getVariables().get("url.form");
        defaultVariables.getVariables().get("url.form");
        Selenide.open(pageUrl);

        // PageObject approach
        new TestFormPage().click("Отмена").checkCondition("Комментарий", Condition.exactText("Comments..."));
    }

    @Test
    void checkNotFrameElem() {

        var pageUrl = defaultVariables.getVariables().get("url.form");
        Selenide.open(pageUrl);

        // PageObject approach
        new NoFrameWorkElementsPage().listsCheck();
    }

    @Test
    void example2() {
        var pageUrl = defaultVariables.getVariables().get("url.form");
        Selenide.open(pageUrl);
        new TestFormPage()
                .click("Отмена")
                .checkCondition("Комментарий", Condition.visible)
                .fillFormAndSend()
                .goTo(MultipleProgressBarsPage.class)
                .checkPage()
                .goTo(TestFormPage.class);
    }

    @Test
    void example3() {
        var pageUrl = defaultVariables.getVariables().get("url.form");
        Selenide.open(pageUrl);
        new TestFormPage()
                .click("Отмена")
                .checkCondition("Комментарий", Condition.visible)
                .fillFormAndSend()
                .goTo(MultipleProgressBarsPage.class)
                .checkPage()
                .goTo(TestFormPage.class);
    }

    @Test
    void example4() {
        var pageUrl = defaultVariables.getVariables().get("url.form");
        Selenide.open(pageUrl);
        new TestFormPage()
                .click("Отмена")
                .checkCondition("Комментарий", Condition.visible)
                .fillFormAndSend()
                .goTo(MultipleProgressBarsPage.class)
                .checkPage()
                .goTo(TestFormPage.class);
    }
}
