package ru.autotestframework.desktop_elements.driver_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriver;
import ru.autotestframework.desktop_elements.driver_manager.drivers.DriverDesktop;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;

@Tag("@DesktopElements")
class DriverManagerDesktopTest {
    private WebDriver driver;
    private final DriverContainerImpl driverContainer = new DriverContainerImplBoot();

    @AfterEach
    void quit() {
        if (Objects.nonNull(driver)) {
            driver.close();
            driver.quit();
        }
    }

    @Disabled("прорабоать вопрос c запуском Desktop-тестов на серверных VM")
    @Test
    void getDesktopDriverTest() {
        String driverPath = "../drivers/FlaNium.Desktop.Driver-v1.1.1/FlaNium.Driver.exe";
        String propertiesPath = "src/test/resources/FlaNiumDriver.properties";
        new DriverDesktop(driverPath, propertiesPath).getDriver();
        assertEquals(System.getProperty("webdriver.desktop.driver"), driverPath);
        assertTrue(driver instanceof DesktopDriver);
    }
}
