package ru.autotestframework.screen_elements.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;

/**
 * Screen driver config test.
 */
@Tag("@ScreenElements")
class ScreenDriverConfigTest {

    /**
     * The Driver container.
     */
    DriverContainer driverContainer = new DriverContainerImplBoot();
    /**
     * The Screen drivers properties.
     */
    ScreenDriverProperties screenDriversProperties = new ScreenDriverProperties();
    /**
     * The Ui properties.
     */
    UiProperties uiProperties = new UiProperties();
    /**
     * The Screen driver config.
     */
    ScreenDriverConfig screenDriverConfig;

    /**
     * Sets .
     */
    @BeforeEach
    public void setup() {
        screenDriversProperties.setPropertiesPath("propertiesPath");
        screenDriversProperties.setPath("path");

        screenDriverConfig = new ScreenDriverConfig(screenDriversProperties, driverContainer, uiProperties);
    }

    /**
     * Sets driver test.
     */
    @Test
    void setDriverTest() {
        ScreenDriverConfig screenDriverConfig =
                new ScreenDriverConfig(screenDriversProperties, driverContainer, uiProperties);
        screenDriverConfig.setDriver();
        Assertions.assertNotNull(driverContainer.getActiveDriver());
        Assertions.assertEquals(System.getProperty("framework.ui.timeout"), String.valueOf(uiProperties.getTimeout()));
        Assertions.assertEquals("false", System.getProperty("java.awt.headless"));
    }

    /**
     * Sets driver with path test.
     */
    @Test
    void setDriverWithPathTest() {
        screenDriverConfig.setDriver();
        Assertions.assertEquals("path", driverContainer.getActiveDriver().getPath());
    }

    /**
     * Sets driver with properties path test.
     */
    @Test
    void setDriverWithPropertiesPathTest() {

        screenDriverConfig.setDriver();
        Assertions.assertEquals(
                "propertiesPath", driverContainer.getActiveDriver().getPropertyPath());
    }
}
