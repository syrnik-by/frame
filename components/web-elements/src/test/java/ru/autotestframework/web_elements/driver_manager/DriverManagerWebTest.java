package ru.autotestframework.web_elements.driver_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.autotestframework.Constants;
import ru.autotestframework.core.exception.ConfigurationException;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImplBoot;
import ru.autotestframework.web_elements.driver_manager.drivers.DriverWeb;

@Tag("@webElemElements")
class DriverManagerWebTest {
    private final DriverContainerImpl driverContainer = new DriverContainerImplBoot() {};
    private Driver driver;

    @AfterEach
    void quit() {
        if (Objects.nonNull(driver)) {
            if (Objects.nonNull(driver.getDriver())) {
                driver.getDriver().close();
                driver.getDriver().quit();
            }
        }
    }

    @Disabled("прорабоать вопрос запуском driver на серверных VM")
    @Test
    void getNotImplementedDriver() {
        String propertiesPath = "src/test/resources/chromedriver.properties";
        driver = new DriverWeb("", propertiesPath);
        assertThrows(ConfigurationException.class, driver::getDriver);
    }

    @Disabled("прорабоать вопрос запуском driver на серверных VM")
    @Test
    void getChromeDriverTest() {
        String driverPath = "../drivers/chromedriver114.exe";
        String propertiesPath = "src/test/resources/chromedriver.properties";
        driver = new DriverWeb(driverPath, propertiesPath);
        driverContainer.add(driver);
        driverContainer.get();
        assertEquals(System.getProperty("webdriver.chrome.driver"), driverPath);
    }

    @Disabled("последняя доступная версия - 99, в текущей версии яндекса - 106")
    @Test
    void getYandexDriverTest() {
        String driverPath = "../drivers/chromedriver99.exe";
        String propertiesPath = "src/test/resources/yandexdriver.properties";
        driver = new DriverWeb(driverPath, propertiesPath);
        assertEquals(System.getProperty("webdriver.chrome.driver"), driverPath);
        assertTrue(driver.getDriver() instanceof ChromeDriver);
    }

    @Disabled("прорабоать вопрос запуском driver на серверных VM")
    @Test
    void checkReuseBrowserEnabled() {
        String driverPath = "./../../drivers/chromedriver114.exe";
        String propertiesPath = "src/test/resources/chromedriver.properties";
        driver = new DriverWeb(driverPath, propertiesPath);
        System.setProperty(Constants.ENABLE_BROWSER_REUSE, String.valueOf(true));

        driverContainer.add(driver);
        WebDriver oldDriver = driverContainer.get();
        driverContainer.get().navigate().to("http://localhost");
        driverContainer.remove();

        assertEquals(oldDriver, driverContainer.get());
    }

    @Disabled("прорабоать вопрос запуском driver на серверных VM")
    @Test
    void checkReuseBrowserDisabled() throws NoSuchFieldException, IllegalAccessException {
        System.setProperty(Constants.ENABLE_BROWSER_REUSE, String.valueOf(false));
        String driverPath = "./../../drivers/chromedriver114.exe";
        String propertiesPath = "src/test/resources/chromedriver.properties";
        DriverWeb driverWeb = new DriverWeb(driverPath, propertiesPath);

        driverContainer.add(driverWeb);
        WebDriver oldDriver = driverContainer.get();
        driverContainer.remove();

        DriverWeb activeDriver = (DriverWeb) driverContainer.getActiveDriver();
        assertNull(activeDriver.getWebDriver());
        assertNotEquals(oldDriver, driverContainer.get());
    }
}
