package ru.autotestframework.desktop_elements.driver_builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.ui_core.driver_builder.Configuration;

@Tag("@DesktopElements")
class DriverBuilderTest {

    private final PropertiesBuilder propertiesBuilder = new PropertiesBuilder();
    private static final String PROPERTIES_PATH = "properties/test.properties";

    @Test
    void driverBuilderWithPropertiesTest() {
        Configuration configuration =
                propertiesBuilder.withProperties(PROPERTIES_PATH).build();
        assertEquals("test", configuration.getProperties().getProperty("test"));
    }

    @Test
    void driverBuilderWithDesiredCapabilitiesTest() {
        Configuration configuration = propertiesBuilder
                .withProperties(PROPERTIES_PATH)
                .withDesiredCapabilities()
                .build();
        assertEquals("test", configuration.getDesiredCapabilities().getCapability("test"));
    }
}
