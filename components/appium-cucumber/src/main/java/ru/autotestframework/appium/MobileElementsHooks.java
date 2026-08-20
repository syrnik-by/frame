package ru.autotestframework.appium;

import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.Before;
import io.cucumber.spring.ScenarioScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;

@Slf4j
@Lazy
@ScenarioScope
@RequiredArgsConstructor
public class MobileElementsHooks {

    @Autowired
    private final DriverContainer driverContainer;

    /**
     * configures WebDriver
     */
    @Before
    public void initFirstPage() {
        WebDriverRunner.setWebDriver(driverContainer.get());
    }
}
