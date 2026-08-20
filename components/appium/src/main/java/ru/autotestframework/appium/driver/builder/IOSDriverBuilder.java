package ru.autotestframework.appium.driver.builder;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.ui_core.driver_builder.Configuration;
import ru.autotestframework.ui_core.driver_builder.IDriverBuilder;
import ru.autotestframework.ui_core.exceptions.InitializationException;

public class IOSDriverBuilder implements IDriverBuilder {

    private final Properties properties;

    public IOSDriverBuilder(final Configuration configuration) {
        this.properties = configuration.getProperties();
    }

    /**
     * bulds new IOS Driver
     * @return
     */
    @Override
    public WebDriver build() {
        try {
            return new IOSDriver(new URL(properties.getProperty("framework.appium.url")), getCaps());
        } catch (MalformedURLException e) {
            throw new InitializationException(e.toString(), e);
        }
    }

    private XCUITestOptions getCaps() {
        return new XCUITestOptions()
                .setPlatformName("iOS")
                .setDeviceName(properties.getProperty("framework.appium.ios.device.name"))
                .setApp(properties.getProperty("framework.appium.ios.app.path"))
                .setAutomationName(properties.getProperty("framework.appium.ios.automation.name"))
                .setPlatformVersion(properties.getProperty("framework.appium.ios.platform.version"));
    }
}
