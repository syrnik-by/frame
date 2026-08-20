package ru.autotestframework.ui_core.configuration;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * An interface that allows you to set the DriverContainerImpl UI for a specific module.
 */
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public interface IDriverSetter {
    /**
     * Sets driver.
     */
    void setDriver();
}
