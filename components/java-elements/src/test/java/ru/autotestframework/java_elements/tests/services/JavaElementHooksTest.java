package ru.autotestframework.java_elements.tests.services;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.java_elements.services.JavaElementsHooks;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;

@Tag("@JavaElements")
public class JavaElementHooksTest {

    @Test
    void setUpTest() {
        DriverContainer driverContainer = new DriverContainerImplBoot();
        JavaElementsHooks javaElementsHooks = new JavaElementsHooks(driverContainer);
        Driver driver = Mockito.mock(Driver.class);
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        Mockito.when(driver.getPath()).thenReturn("driverPath");
        Mockito.when(driver.getDriver()).thenReturn(webDriver);
        driverContainer.add(driver);
        javaElementsHooks.setUp();
        Assertions.assertEquals(WebDriverRunner.getWebDriver(), webDriver);
        Assertions.assertFalse(Boolean.getBoolean(System.getProperty("java.awt.headless")));
    }
}
