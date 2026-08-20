package ru.autotestframework.screen_elements.driver_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.screen_elements.driver_builder.ScreenDriverBuilder;
import ru.autotestframework.screen_elements.driver_manager.drivers.DriverScreen;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;

/**
 * Driver manager screen test.
 */
@Tag("@ScreenElements")
class DriverManagerScreenTest {

    private final DriverContainerImpl driverContainer = new DriverContainerImplBoot();

    /**
     * Remove driver after test.
     */
    @AfterEach
    void quit() {
        driverContainer.remove();
    }

    /**
     * Gets screen driver test.
     */
    @Test
    void getScreenDriverTest() {
        try (MockedConstruction<ScreenDriverBuilder> mockScreen =
                Mockito.mockConstruction(ScreenDriverBuilder.class, (mock, context) -> {
                    when(mock.build()).thenReturn(null);
                })) {
            String driverPath = "";
            String propertiesPath = "screen.properties";
            DriverScreen driverScreen = new DriverScreen(driverPath, propertiesPath);
            WebDriver driver = driverScreen.build();
            assertEquals(System.getProperty("webdriver.screen.driver"), driverPath);
        }
    }
}
