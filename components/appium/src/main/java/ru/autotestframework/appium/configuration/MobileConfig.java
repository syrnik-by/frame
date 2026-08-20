package ru.autotestframework.appium.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import ru.autotestframework.appium.driver.DriverMobile;
import ru.autotestframework.appium.properties.AppiumProperties;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;

@Lazy
@Configuration
@RequiredArgsConstructor
public class MobileConfig implements IDriverSetter {

    private final AppiumProperties appiumProperties;
    private final DriverContainer driverContainer;

    /**
     * sets Mobile Driver
     */
    @Override
    public void setDriver() {
        driverContainer.add(new DriverMobile("", appiumProperties.getPropertiesPath()));
    }
}
