package ru.autotestframework.web_elements.driver_manager.drivers;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;

// TODO перенести в UI core если понадобится параллелизация в других модулях
@Slf4j
public final class WebDriverManager {
    private static final Queue<DriverWeb> availableDrivers = new LinkedList<DriverWeb>();
    private static WebDriverManager INSTANCE;

    private WebDriverManager() {}

    /**
     * WebDriverManager singleton
     * @return
     */
    public static WebDriverManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WebDriverManager();
        }
        return INSTANCE;
    }

    private boolean isDriversLimitExceeded() {
        int counterParallelism = Math.max(
                Integer.getInteger("cucumber.execution.parallel.config.fixed.parallelism", 1),
                Integer.getInteger("junit.jupiter.execution.parallel.config.fixed.parallelism", 1));

        return getAvailableDrivers().size() >= counterParallelism;
    }

    /**
     * Clears drivers queue
     */
    public void clear() {
        log.info("Quit all drivers");
        getAvailableDrivers().forEach(x -> Optional.ofNullable(x.getWebDriver()).ifPresent(WebDriver::quit));
        getAvailableDrivers().clear();
    }

    /**
     * Returns queue of available drivers
     * @return
     */
    public synchronized Queue<DriverWeb> getAvailableDrivers() {
        return availableDrivers;
    }

    /**
     * Removes given driver from queue
     * @param driverWeb
     */
    public synchronized void remove(final DriverWeb driverWeb) {
        availableDrivers.remove(driverWeb);
    }

    /**
     * Returns first driver from queue
     * @return
     */
    @SneakyThrows
    public synchronized DriverWeb getAvailableDriver() {
        return getAvailableDrivers().poll();
    }

    /**
     * Puts driver to queue
     * @param driverWeb
     * @return
     */
    public synchronized boolean putAvailableDriver(final DriverWeb driverWeb) {
        if (isDriversLimitExceeded()) {
            return false;
        }
        getAvailableDrivers().add(driverWeb);
        return isDriversLimitExceeded();
    }

    /**
     * Creates new driver and puts to queue
     * @param driversProperties
     * @return
     */
    public synchronized DriverWeb createAvailableDriver(final WebDriversProperties driversProperties) {
        if (isDriversLimitExceeded()) {
            return null;
        }
        var driver = new DriverWeb(
                DriverDownloader.getInstance().setUpDriver(driversProperties), driversProperties.getPropertiesPath());
        getAvailableDrivers().add(driver);
        return driver;
    }
}
