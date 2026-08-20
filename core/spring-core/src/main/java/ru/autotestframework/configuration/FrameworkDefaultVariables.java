package ru.autotestframework.configuration;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Class that contains all properties pair (key-value) from current environment (system env, spring profile properties etc)
 */
@Component
@ConfigurationProperties(prefix = "framework")
public class FrameworkDefaultVariables {

    @Getter
    private final Map<String, String> variables = new HashMap<>();

    /**
     * Gets map.
     *
     * @return the map
     */
    public Map<String, String> getMap() {
        return variables;
    }
}
