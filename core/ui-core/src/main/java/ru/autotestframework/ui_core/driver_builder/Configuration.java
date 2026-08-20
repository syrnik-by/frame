package ru.autotestframework.ui_core.driver_builder;

import java.util.Properties;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Configuration.
 */
public class Configuration {

    private Properties properties;
    private ChromeOptions chromeOptions;
    private DesiredCapabilities desiredCapabilities;
    private Properties cookies;
    /**
     * Gets properties.
     *
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Sets properties.
     *
     * @param properties the properties
     */
    public void setProperties(final Properties properties) {
        this.properties = properties;
    }

    /**
     * Gets chrome options.
     *
     * @return the chrome options
     */
    public ChromeOptions getChromeOptions() {
        return chromeOptions;
    }

    /**
     * Sets chrome options.
     *
     * @param chromeOptions the chrome options
     */
    public void setChromeOptions(final ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    /**
     * Gets desired capabilities.
     *
     * @return the desired capabilities
     */
    public DesiredCapabilities getDesiredCapabilities() {
        return desiredCapabilities;
    }

    /**
     * Sets desired capabilities.
     *
     * @param desiredCapabilities the desired capabilities
     */
    public void setDesiredCapabilities(final DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
    }

    /**
     * Gets cookies.
     *
     * @return the cookies
     */
    public Properties getCookies() {
        return cookies;
    }

    /**
     * Sets cookies.
     *
     * @param cookies the cookies
     */
    public void setCookies(final Properties cookies) {
        this.cookies = cookies;
    }
}
