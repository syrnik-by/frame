package ru.autotestframework.core.spring;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * registering ThreadScope
 */
@Configuration
public class ThreadScopeConfigurer {
    /**
     * Your custom scope configurer custom scope configurer.
     *
     * @return the custom scope configurer
     */
    @Bean
    public static CustomScopeConfigurer yourCustomScopeConfigurer() {
        final var newConfigurer = new CustomScopeConfigurer();
        Map<String, Object> newScopes = new HashMap<String, Object>();
        newScopes.put("thread", new ThreadScope());
        newConfigurer.setScopes(newScopes);
        return newConfigurer;
    }
}
