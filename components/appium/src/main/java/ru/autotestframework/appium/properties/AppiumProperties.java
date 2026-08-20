package ru.autotestframework.appium.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "framework.appium")
public class AppiumProperties {

    @Value("${framework.appium.properties}")
    private String propertiesPath;
}
