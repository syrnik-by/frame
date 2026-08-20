package ru.autotestframework.autoit_junit.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import ru.autotestframework.autoit_junit.driver_manager.drivers.DriverAutoIt;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;

/**
 * Auto it driver config.
 */
@Lazy
@Configuration
@RequiredArgsConstructor
public class AutoItDriverConfig implements IDriverSetter {
    private final AutoItDriverProperties autoITDriverProperties;
    private final UiProperties uiProperties;
    private final DriverContainerImpl driverContainer;

    /**
     * Sets new driver with properties
     */
    @Override
    public void setDriver() {
        driverContainer.add(new DriverAutoIt("", autoITDriverProperties.getPropertiesPath()));
        System.setProperty("autoit.app.close", String.valueOf(autoITDriverProperties.isCloseApp()));
        System.setProperty("framework.ui.timeout", String.valueOf(uiProperties.getTimeout()));
    }
}
