package ru.autotestframework.desktop_elements.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import ru.autotestframework.desktop_elements.driver_manager.drivers.DriverDesktop;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;

@Lazy
@Configuration
@RequiredArgsConstructor
public class DesktopDriverConfig implements IDriverSetter {
    private final DesktopDriverProperties desktopDriverProperties;
    private final DriverContainer driverContainer;
    private final UiProperties uiProperties;

    /**
     * sets new driver and properties
     */
    @Override
    public void setDriver() {
        driverContainer.add(
                new DriverDesktop(desktopDriverProperties.getPath(), desktopDriverProperties.getPropertiesPath()));
        System.setProperty("desktop.app.close", String.valueOf(desktopDriverProperties.isCloseApp()));
        System.setProperty("framework.ui.timeout", String.valueOf(uiProperties.getTimeout()));
    }
}
