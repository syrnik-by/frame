package ru.autotestframework.ui_core.driver_manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.core.exception.ConfigurationException;

/**
 * Driver container.
 */
@Slf4j
public abstract class DriverContainerImpl implements DriverContainer {
    private final List<Driver> drivers;

    @Getter
    private Driver activeDriver;

    /**
     * Instantiates a new Driver container.
     */
    public DriverContainerImpl() {
        this.drivers = new ArrayList<>();
    }

    /**
     * Add Driver to Container.
     * @param driver new driver for adding
     */
    public void add(final Driver driver) {
        if (!driver.getPath().contains("$")) {
            drivers.add(driver);
            activeDriver = driver;
            System.setProperty("webdriver.chrome.driver", activeDriver.getPath());
        }
    }
    /**
     * Set Active Driver by name.
     * @param setDriverName name for current driver
     */
    public void setByName(final String setDriverName) {
        if (!setDriverName.contains("$")) {
            activeDriver = findDriver(setDriverName);
            System.setProperty("webdriver.chrome.driver", activeDriver.getPath());
        }
    }

    /**
     * @return active Driver.
     */
    public WebDriver get() {
        try {
            return activeDriver.getDriver();
        } catch (NullPointerException npe) {
            throw new ConfigurationException("Driver not set: {}", npe, activeDriver);
        } catch (SessionNotCreatedException snce) {
            log.error(
                    "Retry creating Driver because known bug: https://github.com/SeleniumHQ/selenium/issues/12346",
                    snce);
            return activeDriver.getDriver();
        }
    }

    /**
     * Quit currently active Driver.
     */
    public void remove() {
        if (Objects.nonNull(activeDriver)) {
            quit(activeDriver);
        }
    }

    /**
     * Release (close or withhold for sequential test)
     */
    public void release() {
        drivers.forEach(Driver::release);
    }

    /**
     * Quit Driver by name.
     * @param name given Driver name.
     */
    public void remove(final String name) {
        final var driver = findDriver(name);
        quit(driver);
    }

    public void quit(final Driver driver) {
        driver.quitWebDriver();
    }

    private Driver findDriver(final String driverName) {
        Optional<Driver> findDriver = drivers.stream()
                .filter(driver -> driver.getName().equals(driverName))
                .findFirst();
        if (findDriver.isEmpty()) {
            throw new ConfigurationException("Driver '{}' not set ", driverName);
        }
        return findDriver.get();
    }
}
