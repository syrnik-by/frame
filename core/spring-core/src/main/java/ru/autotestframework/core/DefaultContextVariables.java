package ru.autotestframework.core;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.autotestframework.configuration.FrameworkDefaultVariables;

/**
 * Default context variables.
 */
@Component
@RequiredArgsConstructor

/**
 * Class that contains values from {@link FrameworkDefaultVariables} and saved Context values
 * (from one test to another if needed)
 */
public class DefaultContextVariables {
    private final FrameworkDefaultVariables frameWorkContext;

    @Getter
    private final Map<String, Object> variables = new HashMap<>();

    /**
     * Gets map.
     *
     * @return the map
     */
    public synchronized Map<String, Object> getMap() {
        variables.putAll(frameWorkContext.getMap());
        return variables;
    }

    /**
     * Put all.
     *
     * @param map the map
     */
    public synchronized void putAll(Map<String, Object> map) {
        variables.putAll(map);
    }
}
