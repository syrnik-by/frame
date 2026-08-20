package ru.autotestframework.java_junit.driver_builder;

import org.openqa.selenium.remote.DesiredCapabilities;
import ru.autotestframework.ui_core.driver_builder.CorePropertiesBuilder;

public class PropertiesBuilder extends CorePropertiesBuilder<PropertiesBuilder> {

    /**
     * Set to builder Default DesiredCapabilities.
     * @return self
     */
    public PropertiesBuilder withDesiredCapabilities() {
        DesiredCapabilities dc = new DesiredCapabilities();
        configuration.getProperties().forEach((key, value) -> dc.setCapability((String) key, value));
        configuration.setDesiredCapabilities(dc);
        return this;
    }
}
