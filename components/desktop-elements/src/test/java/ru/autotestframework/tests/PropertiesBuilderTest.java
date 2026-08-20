package ru.autotestframework.tests;

import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.desktop_elements.driver_builder.PropertiesBuilder;
import ru.autotestframework.ui_core.driver_builder.Configuration;

@Tag("@DesktopElements")
class PropertiesBuilderTest {

    @Test
    void withDesiredCapabilitiesTest() {
        PropertiesBuilder propertiesBuilder = Mockito.mock(PropertiesBuilder.class);
        Mockito.when(propertiesBuilder.withDesiredCapabilities()).thenCallRealMethod();
        Configuration configuration = new Configuration();
        Properties properties = new Properties();
        properties.put("key1", "value1");
        properties.put("key2", "value2");
        configuration.setProperties(properties);
        ReflectionTestUtils.setField(propertiesBuilder, "configuration", configuration);
        propertiesBuilder.withDesiredCapabilities();
        Configuration configurationAfter =
                (Configuration) ReflectionTestUtils.getField(propertiesBuilder, "configuration");
        Assertions.assertNotNull(configurationAfter);
        DesiredCapabilities desiredCapabilities = configurationAfter.getDesiredCapabilities();
        Assertions.assertEquals(properties.getProperty("key1"), desiredCapabilities.getCapability("key1"));
        Assertions.assertEquals(properties.getProperty("key2"), desiredCapabilities.getCapability("key2"));
    }
}
