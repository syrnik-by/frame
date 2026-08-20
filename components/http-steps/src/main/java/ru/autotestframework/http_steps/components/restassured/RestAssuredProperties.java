package ru.autotestframework.http_steps.components.restassured;

import io.restassured.path.json.config.JsonPathConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "framework.rest-assured")
public class RestAssuredProperties {

    public static final Integer DEFAULT_TIMEOUT = 60000;

    public static final String TIMEOUT_PROPERTY_NAME = "framework.rest-assured.timeout";
    public static final String LOGS_PROPERTY_NAME = "framework.rest-assured.logs.enabled";

    private Integer timeout = DEFAULT_TIMEOUT;

    @Value("${" + LOGS_PROPERTY_NAME + ":false}")
    private boolean logsEnabled;

    @Value("${framework.rest-assured.numberReturnType:FLOAT_AND_DOUBLE}")
    private JsonPathConfig.NumberReturnType numberReturnType;

    @Value("${framework.rest-assured.prettyPrint.enabled:true}")
    private boolean prettyPrintEnabled;
}
