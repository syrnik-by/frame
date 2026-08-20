package ru.autotestframework.java_junit.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "framework.ui.driver.java")
public class JavaDriverProperties {

    @Value("${framework.ui.driver.java.path:}")
    private String path;

    @Value("${framework.ui.driver.java.properties.path:}")
    private String propertiesPath;
}
