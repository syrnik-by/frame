package ru.autotestframework.ui_core.driver_builder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.SneakyThrows;

/**
 * Core properties builder.
 *
 * @param <B> the type parameter
 */
public abstract class CorePropertiesBuilder<B extends CorePropertiesBuilder<B>> {
    /**
     * The Configuration.
     */
    public final Configuration configuration;

    /**
     * Instantiates a new Core properties builder.
     */
    public CorePropertiesBuilder() {
        configuration = new Configuration();
    }

    /**
     * Set Additional Properties for Configuration using framework properties.
     *
     * @param propertiesPath - path to property file
     * @return self b
     */
    @SneakyThrows
    public B withProperties(final String propertiesPath) {
        var properties = new Properties();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesPath);
                InputStream is2 = ClassLoader.getSystemClassLoader().getResourceAsStream(propertiesPath)) {
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            InputStreamReader isr2 = new InputStreamReader(is2, StandardCharsets.UTF_8);
            properties.load(isr);
            properties.load(isr2);
        }

        configuration.setProperties(properties);
        return (B) this;
    }

    /**
     * Return build Config.
     *
     * @return Configuration configuration
     */
    public Configuration build() {
        return configuration;
    }
}
