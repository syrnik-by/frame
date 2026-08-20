package ru.autotestframework.java_elements.services;

import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;

@RequiredArgsConstructor
public class JavaElementsHooks {
    private final DriverContainer driverContainer;

    @Before(order = 1)
    public void setUp() {
        System.setProperty("java.awt.headless", "false");
        WebDriverRunner.setWebDriver(driverContainer.get());
    }
}
