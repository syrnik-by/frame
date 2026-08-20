package ru.autotestframework.autoit_junit.driver_manager.drivers;

import org.openqa.selenium.WebDriver;
import ru.autotestframework.autoit_junit.driver_builder.AutoItDriverBuilder;
import ru.autotestframework.autoit_junit.elements.typified.TypifiedAutoItElement;
import ru.autotestframework.autoitx.AutoItX;
import ru.autotestframework.ui_core.driver_builder.DefaultPropertiesBuilder;
import ru.autotestframework.ui_core.driver_manager.Driver;

/**
 * Driver auto it.
 */
public class DriverAutoIt extends Driver {

    /**
     * The constant autoItX.
     */
    public static final AutoItX autoItX = new AutoItX();

    /**
     * Instantiates a new Driver auto it.
     *
     * @param path         the path
     * @param propertyPath the property path
     */
    public DriverAutoIt(final String path, final String propertyPath) {
        super(path, propertyPath);
    }

    @Override
    public String getTypifiedElementClassName() {
        return TypifiedAutoItElement.class.getName();
    }

    @Override
    public void pressOnKeyBoard(String keys) {
        autoItX.send(keys, false);
    }

    @Override
    public WebDriver build() {
        DefaultPropertiesBuilder propertiesBuilder = new DefaultPropertiesBuilder().withProperties(getPropertyPath());
        var autoITDriverConfiguration = propertiesBuilder.build();
        return new AutoItDriverBuilder(autoITDriverConfiguration).build();
    }
}
