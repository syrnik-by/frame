package ru.autotestframework.tests.configuration;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;
import ru.autotestframework.web_elements.configuration.WebDriverConfig;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;
import ru.autotestframework.web_elements.driver_manager.drivers.DriverWeb;
import ru.autotestframework.web_elements.driver_manager.drivers.WebDriverManager;

@Disabled
@Tag("@webElemElements")
class WebDriverConfigTest {

    @Test
    void setDriverWhenDriverInitIsFalseTest() {
        WebDriversProperties webDriversProperties = new WebDriversProperties();
        DriverContainerImplBoot driverContainer = new DriverContainerImplBoot();

        WebDriverConfig webDriverConfig = new WebDriverConfig(webDriversProperties, driverContainer);
        webDriverConfig.setDriver();
        Assertions.assertNull(driverContainer.getActiveDriver());
    }

    @Test
    void setDriverWhenDriverInitIsTrueAndNoActiveDriverReuseEnabledTest() {
        WebDriversProperties webDriversProperties = new WebDriversProperties();
        DriverContainerImplBoot driverContainer = new DriverContainerImplBoot();

        webDriversProperties.setReuseBrowserEnabled(true);
        WebDriverConfig webDriverConfig = new WebDriverConfig(webDriversProperties, driverContainer);
        WebDriverManager.getInstance().createAvailableDriver(webDriversProperties);
        webDriverConfig.setDriver();
        Assertions.assertNotNull(driverContainer.getActiveDriver());
    }

    @Test
    void setDriverWhenDriverInitIsTrueAndNoActiveDriverReuseDisabledTest() {
        WebDriversProperties webDriversProperties = new WebDriversProperties();
        DriverContainerImplBoot driverContainer = new DriverContainerImplBoot();

        webDriversProperties.setReuseBrowserEnabled(false);
        WebDriverConfig webDriverConfig = new WebDriverConfig(webDriversProperties, driverContainer);
        webDriverConfig.setDriver();
        Assertions.assertNotNull(driverContainer.getActiveDriver());
    }

    @Test
    void setDriverWhenDriverInitIsTrueAndActiveDriverExistsTest() {
        WebDriversProperties webDriversProperties = new WebDriversProperties();
        DriverContainerImplBoot driverContainer = new DriverContainerImplBoot();

        DriverWeb availableDriver = WebDriverManager.getInstance().createAvailableDriver(webDriversProperties);
        Assertions.assertNotNull(availableDriver);
        driverContainer.add(availableDriver);
        WebDriverConfig webDriverConfig = new WebDriverConfig(webDriversProperties, driverContainer);
        webDriverConfig.setDriver();
        Assertions.assertEquals(driverContainer.getActiveDriver(), availableDriver);
    }

    @AfterEach
    void removeDriverAfter() {
        WebDriverManager.getInstance().clear();
    }
}
