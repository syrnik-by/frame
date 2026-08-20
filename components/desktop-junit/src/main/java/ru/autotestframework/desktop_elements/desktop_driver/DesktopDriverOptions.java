package ru.autotestframework.desktop_elements.desktop_driver;

import org.openqa.selenium.Capabilities;

/**
 * Defines the interface to manage options specific to {@link DesktopDriver}.
 */
public interface DesktopDriverOptions {
    /**
     * Convert options to DesiredCapabilities for one of DesktopDrivers.
     * @return The DesiredCapabilities for DesktopDriver with these options.
     */
    Capabilities toCapabilities();
}
