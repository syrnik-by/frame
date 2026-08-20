package ru.autotestframework.web_elements.driver_manager;

import static io.cucumber.junit.platform.engine.Constants.PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME;

import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;
import ru.autotestframework.web_elements.driver_manager.drivers.DriverWeb;
import ru.autotestframework.web_elements.driver_manager.drivers.WebDriverManager;

@Tag("@webElemElements")
class DriverManagerTest {
    static String driverPath = "./../../drivers/chromedriver114.exe";
    static String propertiesPath = "src/test/resources/chromedriver.properties";
    static DriverWeb driver;
    static WebDriversProperties props = new WebDriversProperties();

    @BeforeEach
    public void setProp() {
        System.setProperty(PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME, "1");
        driver = new DriverWeb(driverPath, propertiesPath);
        props.setPropertiesPath(propertiesPath);
        props.setPath(driverPath);
    }

    @AfterEach
    public void clean() {
        WebDriverManager.getInstance().clear();
    }

    @Test
    void checkPutLimitation() {
        boolean success = WebDriverManager.getInstance().putAvailableDriver(driver);
        Assertions.assertTrue(success);
        DriverWeb driver2 = new DriverWeb(driverPath, propertiesPath);
        boolean fault = WebDriverManager.getInstance().putAvailableDriver(driver2);
        Assertions.assertFalse(fault);
    }

    @Test
    void checkDriverCreationLimitation() {
        DriverWeb driver = WebDriverManager.getInstance().createAvailableDriver(props);
        Assertions.assertNotNull(driver);
        DriverWeb fault = WebDriverManager.getInstance().createAvailableDriver(props);
        Assertions.assertNull(fault);
    }

    @Test
    void checkParallelCounter() {
        Number threadCount = Faker.instance().number().numberBetween(2, 4);
        System.setProperty(PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME, threadCount.toString());
        int iter = 0;

        while (iter < threadCount.intValue()) {
            iter++;
            DriverWeb driverCreated = WebDriverManager.getInstance().createAvailableDriver(props);
            Assertions.assertNotNull(driverCreated);
        }

        DriverWeb fault = WebDriverManager.getInstance().createAvailableDriver(props);
        Assertions.assertNull(fault);
    }
}
