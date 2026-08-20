package ru.autotestframework.desktop_elements.driver_builder;

import java.io.File;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriver;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriverService;
import ru.autotestframework.desktop_elements.driver_manager.drivers.DriverDesktop;
import ru.autotestframework.ui_core.driver_builder.Configuration;
import ru.autotestframework.ui_core.driver_builder.IDriverBuilder;

public class DesktopDriverBuilder implements IDriverBuilder {

    private final Configuration configuration;

    public DesktopDriverBuilder(final Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * returns new driver with properties
     * @return
     */
    @Override
    @SneakyThrows
    public WebDriver build() {
        DesktopDriverService service = new DesktopDriverService.Builder()
                .usingDriverExecutable(new File(DriverDesktop.staticPath).getAbsoluteFile())
                .usingAnyFreePort()
                .withVerbose(false)
                .withSilent(false)
                .buildDesktopService();
        service.start();
        return new DesktopDriver(service, configuration.getDesiredCapabilities());
    }
}
