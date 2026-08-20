package ru.autotestframework.java_junit.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import ru.autotestframework.java_junit.driver_manager.drivers.DriverJava;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;

@Configuration
@RequiredArgsConstructor
public class JavaDriverConfig implements IDriverSetter {
    private final JavaDriverProperties javaDriverProperties;
    private final DriverContainer driverContainer;
    private final UiProperties uiProperties;

    @Override
    public void setDriver() {
        driverContainer.add(new DriverJava(javaDriverProperties.getPath(), javaDriverProperties.getPropertiesPath()));
        System.setProperty("framework.ui.timeout", String.valueOf(uiProperties.getTimeout()));
    }
}
