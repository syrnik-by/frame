package ru.autotestframework.core.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.DefaultContextVariables;

/**
 * Context.
 */
@Data
@Slf4j
@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ContextImpl implements Context, Savable {

    private final DefaultContextVariables defaultVariables;
    private final Map<String, Object> contextVariables = new HashMap<>();

    @Override
    public <T extends Object> T getObj(final String varName) {

        if (contextVariables.containsKey(varName)) {
            return (T) contextVariables.get(varName);
        } else if (defaultVariables.getMap().containsKey(varName)) {
            return (T) defaultVariables.getMap().get(varName);
        } else if (!"false".equals(System.getProperty(varName, "false"))) {
            return (T) System.getProperty(varName);
        } else if (Objects.nonNull(System.getenv(varName))) {
            return (T) System.getenv(varName);
        } else {
            log.error("Variable '{}' is missing in Context", varName);
            return null;
        }
    }

    @Override
    public String get(final String varName) {
        return (getObj(varName)) == null ? null : (getObj(varName)).toString();
    }

    @Override
    public Map<String, Object> getAll() {
        var resultMap = new HashMap<String, Object>(defaultVariables.getMap());
        resultMap.putAll(contextVariables);
        return resultMap;
    }

    @Override
    public void set(final String varName, final Object value) {
        if (value == null) {
            log.warn("Variable {} is null", varName);
            contextVariables.put(varName, null);
        } else {
            contextVariables.put(varName, value);
        }
    }

    @Override
    public void set(final Map<String, ?> variables) {
        variables.forEach(this::set);
    }

    @Override
    public void set(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs) {
        this.set(firstParameterName, firstParameterValue);
        if (parameterNameValuePairs != null) {
            for (var i = 0; i < parameterNameValuePairs.length; i = i + 2) {
                this.set(parameterNameValuePairs[i].toString(), parameterNameValuePairs[i + 1]);
            }
        }
    }

    @Override
    public void clean() {
        contextVariables.clear();
        log.info("Context Variables were cleared");
    }

    @Override
    public void save() {
        defaultVariables.putAll(contextVariables);
    }
}
