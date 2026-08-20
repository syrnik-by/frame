package ru.autotestframework.web_elements.driver_builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.PageLoadStrategy;
import ru.autotestframework.ui_core.driver_builder.Configuration;

@Tag("@webElemElements")
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
    void driverBuilderWithChromeOptionsTest() {
        Configuration configuration = propertiesBuilder
                .withProperties(PROPERTIES_PATH)
                .withChromeOptions()
                .build();
        assertEquals(PageLoadStrategy.NONE, configuration.getChromeOptions().getCapability("pageLoadStrategy"));
    }
}
