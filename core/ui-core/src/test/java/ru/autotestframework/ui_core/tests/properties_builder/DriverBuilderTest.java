package ru.autotestframework.ui_core.tests.properties_builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.ui_core.driver_builder.Configuration;
import ru.autotestframework.ui_core.driver_builder.DefaultPropertiesBuilder;

/**
 * Driver builder test.
 */
@Tag("@PageManagerTest")
class DriverBuilderTest {
    private final DefaultPropertiesBuilder propertiesBuilder = new DefaultPropertiesBuilder();
    private static final String PROPERTIES_PATH = "properties/test.properties";

    /**
     * Driver builder with properties test.
     */
    @Test
    void driverBuilderWithPropertiesTest() {
        Configuration configuration =
                propertiesBuilder.withProperties(PROPERTIES_PATH).build();
        assertEquals("test", configuration.getProperties().getProperty("test"));
    }
}
