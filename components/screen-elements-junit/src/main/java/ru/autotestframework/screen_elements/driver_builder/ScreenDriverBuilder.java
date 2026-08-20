package ru.autotestframework.screen_elements.driver_builder;

import org.openqa.selenium.WebDriver;
import ru.autotestframework.screen_elements.driver_manager.drivers.SikuliDriver;
import ru.autotestframework.ui_core.driver_builder.Configuration;
import ru.autotestframework.ui_core.driver_builder.IDriverBuilder;

/**
 * Screen driver builder.
 */
public class ScreenDriverBuilder implements IDriverBuilder {

    private final Configuration configuration;

    /**
     * Instantiates a new Screen driver builder.
     *
     * @param configuration the configuration
     */
    public ScreenDriverBuilder(final Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Builds WebDriver with needed configuration
     * @return WebDriver
     */
    @Override
    public WebDriver build() {
        return new SikuliDriver(configuration.getProperties().getProperty("app"));
    }
}
