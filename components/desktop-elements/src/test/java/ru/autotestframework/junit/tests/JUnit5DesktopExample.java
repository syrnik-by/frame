package ru.autotestframework.junit.tests;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.pages.Other;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.junit.BaseUITest;

@Disabled
@Tag("@DesktopElements")
class JUnit5DesktopExample extends BaseUITest {

    @Autowired
    PageManager pageManager;

    @Autowired
    FrameworkDefaultVariables defaultVariables;

    @Autowired
    DriverContainer driverContainer;

    @Test
    void exampleTest() {
        TypifiedDesktopElement b = pageManager.getPageByClass(Other.class).getElementByTitle("Text");
        $(b.getSelenideElement()).shouldBe(Condition.enabled);
    }
}
