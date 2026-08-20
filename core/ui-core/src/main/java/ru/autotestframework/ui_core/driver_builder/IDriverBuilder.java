package ru.autotestframework.ui_core.driver_builder;

import org.openqa.selenium.WebDriver;

/**
 * Driver builder.
 */
public interface IDriverBuilder {

    /**
     * Build web driver.
     *
     * @return the web driver
     */
    WebDriver build();
}
