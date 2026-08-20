package ru.autotestframework.http_steps.components;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mockservice")
public class MockServiceProperties {

    public static final String PORT_PROPERTY_NAME = "mockservice.port";
    public static final Integer DEFAULT_PORT = 8484;

    private Integer port = DEFAULT_PORT;
}
