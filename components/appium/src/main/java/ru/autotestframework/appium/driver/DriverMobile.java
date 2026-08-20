package ru.autotestframework.appium.driver;

import org.openqa.selenium.WebDriver;
import ru.autotestframework.appium.driver.builder.AndroidDriverBuilder;
import ru.autotestframework.appium.driver.builder.IOSDriverBuilder;
import ru.autotestframework.appium.elements.typified.TypifiedMobileElement;
import ru.autotestframework.ui_core.driver_builder.DefaultPropertiesBuilder;
import ru.autotestframework.ui_core.driver_manager.Driver;
import ru.autotestframework.ui_core.exceptions.InitializationException;

public class DriverMobile extends Driver {

    @Override
    public String getTypifiedElementClassName() {
        return TypifiedMobileElement.class.getName();
    }

    public DriverMobile(String path, String propertyPath) {
        super(path, propertyPath);
    }

    /**
     * builds new Mobile Driver with properties
     * @return
     */
    @Override
    public WebDriver build() {
        var configuration =
                new DefaultPropertiesBuilder().withProperties(getPropertyPath()).build();
        String platform = configuration.getProperties().getProperty("framework.appium.platform");
        if (platform.equals("android")) {
            return new AndroidDriverBuilder(configuration).build();
        } else if (platform.equals("ios")) {
            return new IOSDriverBuilder(configuration).build();
        } else {
            throw new InitializationException(
                    "Не найдем мобильный драйвер с типом '{}', параметр framework.appium.platform, доступные типы 'android, ios'",
                    platform);
        }
    }
}
