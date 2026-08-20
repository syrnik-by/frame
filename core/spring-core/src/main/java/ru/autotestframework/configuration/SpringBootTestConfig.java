package ru.autotestframework.configuration;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring boot test config.
 */
@SpringBootTest(
        classes = FrameworkConfig.class,
        properties = {
            "spring.config.name=framework",
            "spring.profiles.default=" + SpringBootTestConfig.DEFAULT_PROFILE,
            "spring.main.banner-mode=off"
        })
public class SpringBootTestConfig {
    /**
     * The constant DEFAULT_PROFILE.
     */
    public static final String DEFAULT_PROFILE = "local";
}
