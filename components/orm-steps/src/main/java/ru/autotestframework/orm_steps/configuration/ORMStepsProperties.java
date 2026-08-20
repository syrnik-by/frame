package ru.autotestframework.orm_steps.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Orm steps properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "framework.orm")
public class ORMStepsProperties {

    /**
     * The path to the file with properties
     */
    @Value("${framework.orm.properties.path}")
    private String propertiesPath;

    /**
     * A package for scanning classes with database objects
     */
    @Value("${framework.orm.package.path}")
    private String packagePath;
}
