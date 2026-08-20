package ru.autotestframework.ui_core.tests.driver_builder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.ui_core.driver_builder.Configuration;
import ru.autotestframework.ui_core.driver_builder.DefaultPropertiesBuilder;

/**
 * Default properties builder test.
 */
@Tag("@UiCore")
public class DefaultPropertiesBuilderTest {

    /**
     * With properties test.
     */
    @Test
    void withPropertiesTest() {
        DefaultPropertiesBuilder defaultPropertiesBuilder = new DefaultPropertiesBuilder();
        defaultPropertiesBuilder.withProperties("chromedriver.properties");
        Configuration configuration =
                (Configuration) ReflectionTestUtils.getField(defaultPropertiesBuilder, "configuration");
        Assertions.assertNotNull(configuration);
        Assertions.assertEquals("none", configuration.getProperties().getProperty("pageLoadStrategy"));
    }

    /**
     * Build test.
     */
    @Test
    void buildTest() {
        DefaultPropertiesBuilder defaultPropertiesBuilder = new DefaultPropertiesBuilder();
        Configuration configuration = Mockito.mock(Configuration.class);
        ReflectionTestUtils.setField(defaultPropertiesBuilder, "configuration", configuration);
        Assertions.assertEquals(configuration, defaultPropertiesBuilder.build());
    }
}
