package ru.autotestframework.steps;

import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.web_elements.driver_manager.drivers.DriverWeb;

/**
 * Steps.
 */
@RequiredArgsConstructor
public class Steps {
    private final DriverContainer driverContainer;
    /**
     * The Driver.
     */
    static Driver driver;

    /**
     * Open browser.
     *
     * @param url the url
     */
    @When("перейти в браузер по ссылке {resolvable_string}")
    @Sample("Переключить драйвер и с его помощью открыть браузер и перейти по ссылке ")
    @Parameter(type = "resolvable_string", name = "ссылка для открытия браузера")
    public void openBrowser(String url) {
        driver = driverContainer.getActiveDriver();
        driverContainer.add(new DriverWeb("../../drivers/chromedriver116.exe", "framework-web.properties"));
        // Selenide setter
        driverContainer.get().get(url);
    }

    /**
     * Open desk.
     */
    @When("вернуться к десктоп-приложению")
    @Sample("переключиться на драйвер работы с DeskTop")
    public void openDesk() {
        driverContainer.get().quit();
        // переподключаемся
        driverContainer.setByName(driver.getName());
        WebDriverRunner.setWebDriver(driverContainer.get());
        TypifiedDesktopElement.setDriver(driverContainer.get());
    }
}
