package ru.psb.autotestframework.java_junit.tests.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.java_junit.configuration.JavaDriverConfig;
import ru.autotestframework.java_junit.configuration.JavaDriverProperties;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;

@Tag("@JavaElements")
public class JavaDriverConfigTest {

    @Test
    void setDriverTest() {
        DriverContainer driverContainer = new DriverContainerImplBoot();
        JavaDriverProperties javaDriverProperties = new JavaDriverProperties();
        javaDriverProperties.setPath("");
        javaDriverProperties.setPropertiesPath("properties/test.properties");
        UiProperties uiProperties = new UiProperties();
        JavaDriverConfig javaDriverConfig = new JavaDriverConfig(javaDriverProperties, driverContainer, uiProperties);

        javaDriverConfig.setDriver();

        Assertions.assertEquals(driverContainer.getActiveDriver().getPath(), javaDriverProperties.getPath());
        Assertions.assertEquals(
                driverContainer.getActiveDriver().getPropertyPath(), javaDriverProperties.getPropertiesPath());
        Assertions.assertEquals(System.getProperty("framework.ui.timeout"), String.valueOf(uiProperties.getTimeout()));
    }
}
