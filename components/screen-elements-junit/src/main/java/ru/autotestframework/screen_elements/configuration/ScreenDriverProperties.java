package ru.autotestframework.screen_elements.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Screen driver properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "framework.ui.driver.screen")
public class ScreenDriverProperties {

    @Value("${framework.ui.driver.screen.path:}")
    private String path;

    @Value("${framework.ui.driver.screen.properties:}")
    private String propertiesPath;
}
