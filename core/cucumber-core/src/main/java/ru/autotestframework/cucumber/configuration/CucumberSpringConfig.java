package ru.autotestframework.cucumber.configuration;

import static ru.autotestframework.configuration.PlaceholderResolverConfig.EMPTY_STRING_PLACEHOLDER_PROPERTY;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import ru.autotestframework.configuration.FrameworkConfig;

/**
 * Cucumber spring config.
 */
@SpringBootTest(
        classes = FrameworkConfig.class,
        properties = {
            "spring.config.name=framework",
            "spring.profiles.default=" + CucumberSpringConfig.DEFAULT_PROFILE,
            "spring.main.banner-mode=off",
            EMPTY_STRING_PLACEHOLDER_PROPERTY
        })
@CucumberContextConfiguration
public class CucumberSpringConfig {
    /**
     * The constant DEFAULT_PROFILE.
     */
    public static final String DEFAULT_PROFILE = "local";
}
