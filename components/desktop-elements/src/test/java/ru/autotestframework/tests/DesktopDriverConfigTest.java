package ru.autotestframework.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.desktop_elements.configuration.DesktopDriverConfig;
import ru.autotestframework.desktop_elements.configuration.DesktopDriverProperties;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;

@Tag("@DesktopElements")
class DesktopDriverConfigTest {

    @Test
    void setDriverTest() {
        DesktopDriverProperties desktopDriverProperties = new DesktopDriverProperties();
        DriverContainerImplBoot driverContainer = new DriverContainerImplBoot();
        DesktopDriverConfig desktopDriverConfig =
                new DesktopDriverConfig(desktopDriverProperties, driverContainer, new UiProperties());
        desktopDriverProperties.setPath("Path");
        desktopDriverProperties.setPropertiesPath("PropertiesPath");
        desktopDriverProperties.setCloseApp(true);
        desktopDriverConfig.setDriver();
        Driver activeDriver = driverContainer.getActiveDriver();
        Assertions.assertEquals(activeDriver.getPath(), desktopDriverProperties.getPath());
        Assertions.assertEquals(activeDriver.getPropertyPath(), desktopDriverProperties.getPropertiesPath());
        Assertions.assertEquals(
                System.getProperty("desktop.app.close"), String.valueOf(desktopDriverProperties.isCloseApp()));
    }
}
