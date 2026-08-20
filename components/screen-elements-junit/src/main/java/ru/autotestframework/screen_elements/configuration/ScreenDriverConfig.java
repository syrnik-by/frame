package ru.autotestframework.screen_elements.configuration;

import lombok.RequiredArgsConstructor;
import org.sikuli.basics.Settings;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import ru.autotestframework.screen_elements.driver_manager.drivers.DriverScreen;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;

/**
 * Screen driver config.
 */
@Lazy
@Configuration
@RequiredArgsConstructor
public class ScreenDriverConfig implements IDriverSetter {

    private final ScreenDriverProperties screenDriverProperties;
    private final DriverContainer driverContainer;
    private final UiProperties uiProperties;

    /**
     * Sets new driver with properties
     */
    @Override
    public void setDriver() {
        if (!screenDriverProperties.getPropertiesPath().isBlank()) {
            driverContainer.add(
                    new DriverScreen(screenDriverProperties.getPath(), screenDriverProperties.getPropertiesPath()));
        }
        Settings.MoveMouseDelay = 0f;
        Settings.AutoDetectKeyboardLayout = false;
        System.setProperty("framework.ui.timeout", String.valueOf(uiProperties.getTimeout()));
        System.setProperty("java.awt.headless", "false");
    }
}
