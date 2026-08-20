package ru.autotestframework.desktop_elements.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "framework.ui.driver.desktop")
public class DesktopDriverProperties {

    @Value("${framework.ui.driver.desktop.path}")
    private String path;

    @Value("${framework.ui.driver.desktop.properties.path}")
    private String propertiesPath;

    @Value("${framework.ui.driver.desktop.app.close:true}")
    private boolean closeApp;
}
