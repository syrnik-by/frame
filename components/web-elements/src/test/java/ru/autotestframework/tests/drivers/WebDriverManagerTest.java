package ru.autotestframework.tests.drivers;

import java.util.LinkedList;
import java.util.Queue;
import org.junit.jupiter.api.*;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;
import ru.autotestframework.web_elements.driver_manager.drivers.DriverWeb;
import ru.autotestframework.web_elements.driver_manager.drivers.WebDriverManager;

@Tag("@webElemElements")
class WebDriverManagerTest {

    DriverWeb driverWeb;
    WebDriversProperties webDriversProperties = new WebDriversProperties();

    @BeforeEach
    public void setup() {
        driverWeb = new DriverWeb("", "");
    }

    @Test
    void putAvailableDriverPositiveTest() {
        boolean status = WebDriverManager.getInstance().putAvailableDriver(driverWeb);
        Assertions.assertTrue(status);
        System.out.println(driverWeb + " putAvailableDriverPositiveTest");
    }

    @Test
    void putAvailableDriverNegativeTest() {
        DriverWeb driverWeb2 = driverWeb = new DriverWeb("", "");
        WebDriverManager.getInstance().putAvailableDriver(driverWeb);
        boolean status = WebDriverManager.getInstance().putAvailableDriver(driverWeb2);
        Assertions.assertFalse(status);
    }

    @Test
    void getAvailableDriversTest() {
        Queue<DriverWeb> availableDrivers = new LinkedList<>();
        availableDrivers.add(driverWeb);
        WebDriverManager.getInstance().putAvailableDriver(driverWeb);
        Assertions.assertEquals(WebDriverManager.getInstance().getAvailableDrivers(), availableDrivers);
    }

    @Test
    void getAvailableDriverTest() {
        Queue<DriverWeb> availableDrivers = new LinkedList<DriverWeb>();
        availableDrivers.add(driverWeb);
        WebDriverManager.getInstance().putAvailableDriver(driverWeb);
        Assertions.assertEquals(WebDriverManager.getInstance().getAvailableDriver(), availableDrivers.poll());
    }

    @Test
    void removeTest() {
        Queue<DriverWeb> availableDrivers = new LinkedList<DriverWeb>();
        availableDrivers.add(driverWeb);
        WebDriverManager.getInstance().putAvailableDriver(driverWeb);
        WebDriverManager.getInstance().remove(driverWeb);
        Assertions.assertEquals(
                0, WebDriverManager.getInstance().getAvailableDrivers().size());
    }

    @Test
    void createAvailableDriverNegativeTest() {
        WebDriverManager.getInstance().putAvailableDriver(driverWeb);
        DriverWeb driverWebGet = WebDriverManager.getInstance().createAvailableDriver(webDriversProperties);
        Assertions.assertNull(driverWebGet);
    }

    @AfterEach
    void removeDriverAfter() {
        WebDriverManager.getInstance().clear();
    }
}
