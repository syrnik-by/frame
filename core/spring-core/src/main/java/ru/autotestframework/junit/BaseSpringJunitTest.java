package ru.autotestframework.junit;

import static ru.autotestframework.configuration.PlaceholderResolverConfig.EMPTY_STRING_PLACEHOLDER_PROPERTY;

import org.springframework.boot.test.context.SpringBootTest;
import ru.autotestframework.configuration.FrameworkConfig;
import ru.autotestframework.configuration.SpringBootTestConfig;

/**
 * Base TestClass to extend any junit test from. Configure some default settings.
 */
@SpringBootTest(
        classes = {ApplicationMain.class, FrameworkConfig.class},
        properties = {
            "spring.config.name=framework",
            "spring.profiles.default=" + SpringBootTestConfig.DEFAULT_PROFILE,
            "spring.main.banner-mode=off",
            EMPTY_STRING_PLACEHOLDER_PROPERTY
        })
public abstract class BaseSpringJunitTest {}
