package ru.autotestframework.autoit_junit.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Auto it driver properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "framework.ui.driver.autoit")
public class AutoItDriverProperties {

    @Value("${framework.ui.driver.autoit.properties}")
    private String propertiesPath;

    @Value("${framework.ui.driver.autoit.app.close:true}")
    private boolean closeApp;
}
