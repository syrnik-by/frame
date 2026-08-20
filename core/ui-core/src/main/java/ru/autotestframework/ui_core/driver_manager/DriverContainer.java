package ru.autotestframework.ui_core.driver_manager;

import org.openqa.selenium.WebDriver;

/**
 * Driver container.
 */
public interface DriverContainer {
    /**
     * Add.
     *
     * @param driver the driver
     */
    void add(Driver driver);

    /**
     * Sets by name.
     *
     * @param setDriverName the set driver name
     */
    void setByName(String setDriverName);

    /**
     * Get web driver.
     *
     * @return the web driver
     */
    WebDriver get();

    /**
     * Remove.
     */
    void remove();

    /**
     * Sets active driver by name.
     *
     * @param setDriverName the set driver name
     */
    default void setActiveDriverByName(String setDriverName) {
        setByName(setDriverName);
    }

    /**
     * Remove.
     *
     * @param name the name
     */
    void remove(String name);

    /**
     * Release.
     */
    void release();

    /**
     * Quit.
     *
     * @param driver the driver
     */
    default void quit(Driver driver) {
        driver.quitWebDriver();
    }

    /**
     * Gets active driver.
     *
     * @return the active driver
     */
    Driver getActiveDriver();
}
