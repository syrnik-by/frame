package ru.autotestframework.web_elements.configuration;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.web_elements.driver_manager.drivers.DriverWeb;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WebDriverConfig implements IDriverSetter {
    private final WebDriversProperties webDriversProperties;

    private final DriverContainer driverContainer;

    /**
     * Sets new driver with properties
     */
    @Override
    @SneakyThrows
    public synchronized void setDriver() {
        if (Objects.isNull(driverContainer.getActiveDriver())) {
            Driver driver = new DriverWeb(webDriversProperties.getPath(), webDriversProperties.getPropertiesPath());
            driverContainer.add(driver);
            driver.build();
        }
    }
}
