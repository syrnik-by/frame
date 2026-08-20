package ru.psb.autotestframework.java_junit.driver_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import net.sourceforge.marathon.javadriver.JavaDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.java_junit.driver_manager.drivers.DriverJava;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;

@Tag("@JavaElements")
class DriverManagerJavaTest {
    private WebDriver driver;
    private final DriverContainer driverContainer = new DriverContainerImplBoot();

    @AfterEach
    void quit() {
        if (Objects.nonNull(driver)) {
            driver.close();
            driver.quit();
        }
    }

    @Disabled("прорабоать вопрос запуском Java-тестов на серверных VM или мокать драйвер")
    @Test
    void getJavaDriverTest() {
        String driverPath = "";
        String propertiesPath = "src/test/resources/javadriver.properties";
        DriverJava driverJava = new DriverJava(driverPath, propertiesPath);
        driverContainer.add(driverJava);
        driver = driverContainer.get();
        assertEquals(System.getProperty("webdriver.java.driver"), driverPath);
        assertTrue(driver instanceof JavaDriver);
    }
}
