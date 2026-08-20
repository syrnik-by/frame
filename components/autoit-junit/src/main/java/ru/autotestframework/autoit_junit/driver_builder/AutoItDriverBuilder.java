package ru.autotestframework.autoit_junit.driver_builder;

import com.jacob.com.LibraryLoader;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.autoit_junit.driver_manager.drivers.AutoItXDriver;
import ru.autotestframework.ui_core.driver_builder.Configuration;
import ru.autotestframework.ui_core.driver_builder.IDriverBuilder;

/**
 * Auto it driver builder.
 */
public class AutoItDriverBuilder implements IDriverBuilder {

    private final Configuration configuration;

    /**
     * Instantiates a new Auto it driver builder.
     *
     * @param configuration the configuration
     */
    public AutoItDriverBuilder(final Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Builds WebDriver with needed configuration
     * @return WebDriver
     */
    @Override
    public WebDriver build() {
        LibraryLoader.loadJacobLibrary();
        return new AutoItXDriver(configuration.getProperties().getProperty("app"));
    }
}
