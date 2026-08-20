package ru.autotestframework.ui_core.tests.driver_manager;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;

/**
 * Driver container test.
 */
@Tag("@UiCore")
class DriverContainerTest {

    /**
     * Add test.
     */
    @Test
    void addTest() {
        DriverContainer driverContainer = new DriverContainerImplBoot();
        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.getPath()).thenReturn("driverPath");
        driverContainer.add(driver);
        Assertions.assertEquals(driverContainer.getActiveDriver(), driver);
    }

    /**
     * Sets test.
     */
    @Test
    void setTest() {
        DriverContainerImpl driverContainer = new DriverContainerImplBoot();

        Driver driver1 = Mockito.mock(Driver.class);
        Driver driver2 = Mockito.mock(Driver.class);
        Mockito.when(driver1.getName()).thenReturn("1");
        Mockito.when(driver2.getName()).thenReturn("2");
        List<Driver> driverList = List.of(driver1, driver2);
        ReflectionTestUtils.setField(driverContainer, "drivers", driverList);
        ReflectionTestUtils.setField(driverContainer, "activeDriver", driver1);
        Mockito.when(driver2.getPath()).thenReturn("driverPath");
        driverContainer.setByName("2");
        Assertions.assertEquals(driverContainer.getActiveDriver(), driver2);
        Assertions.assertEquals(System.getProperty("webdriver.chrome.driver"), driver2.getPath());
    }

    /**
     * Gets positive test.
     */
    @Test
    void getPositiveTest() {
        DriverContainer driverContainer = new DriverContainerImplBoot();
        Driver driver = Mockito.mock(Driver.class);
        ReflectionTestUtils.setField(driverContainer, "activeDriver", driver);
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        Mockito.when(driver.getDriver()).thenReturn(webDriver);
        WebDriver webDriverFromDriverContainer = driverContainer.get();
        Assertions.assertEquals(webDriver, webDriverFromDriverContainer);
    }

    /**
     * Gets negative test.
     */
    @Test
    void getNegativeTest() {
        DriverContainer driverContainer = new DriverContainerImplBoot();
        Assertions.assertThrows(ConfigurationException.class, driverContainer::get);
    }

    /**
     * Remove 0 arguments test.
     */
    @Test
    void remove0ArgumentsTest() {
        DriverContainer driverContainer = new DriverContainerImplBoot();
        Driver driver = Mockito.mock(Driver.class);
        ReflectionTestUtils.setField(driverContainer, "activeDriver", driver);
        driverContainer.remove();
        Mockito.doThrow(Error.class).when(driver).quitWebDriver();
        Assertions.assertThrows(Error.class, driverContainer::remove);
    }

    /**
     * Remove 1 arguments test.
     */
    @Test
    void remove1ArgumentsTest() {
        DriverContainer driverContainer = new DriverContainerImplBoot();
        Driver driver1 = Mockito.mock(Driver.class);
        Driver driver2 = Mockito.mock(Driver.class);
        Mockito.when(driver1.getName()).thenReturn("1");
        Mockito.when(driver2.getName()).thenReturn("2");
        List<Driver> driverList = List.of(driver1, driver2);
        ReflectionTestUtils.setField(driverContainer, "drivers", driverList);
        driverContainer.remove("2");
        Mockito.doThrow(Error.class).when(driver2).quitWebDriver();
        Assertions.assertThrows(Error.class, () -> driverContainer.remove("2"));
    }
}
