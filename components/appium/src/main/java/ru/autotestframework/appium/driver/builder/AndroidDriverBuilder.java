package ru.autotestframework.appium.driver.builder;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import ru.autotestframework.ui_core.driver_builder.Configuration;
import ru.autotestframework.ui_core.driver_builder.IDriverBuilder;
import ru.autotestframework.ui_core.exceptions.InitializationException;

public class AndroidDriverBuilder implements IDriverBuilder {

    private final Properties properties;

    public AndroidDriverBuilder(final Configuration configuration) {
        this.properties = configuration.getProperties();
    }

    /**
     * builds new Android Driver
     * @return
     */
    @Override
    public WebDriver build() {
        try {
            return new AndroidDriver(new URL(properties.getProperty("framework.appium.url")), getCaps());
        } catch (MalformedURLException e) {
            throw new InitializationException(e.toString(), e);
        }
    }

    private UiAutomator2Options getCaps() {
        return new UiAutomator2Options()
                .setPlatformName("Android")
                .setDeviceName(properties.getProperty("framework.appium.android.device.name"))
                .setApp(properties.getProperty("framework.appium.android.app.path"))
                .setAutomationName(properties.getProperty("framework.appium.android.automation.name"))
                .setPlatformVersion(properties.getProperty("framework.appium.android.platform.version"));
    }
}
