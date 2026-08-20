package ru.autotestframework.web_elements.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import ru.autotestframework.Constants;

@RequiredArgsConstructor
@SpringBootConfiguration
@Configuration
@ComponentScan(basePackages = {Constants.DEFAULT_GLUE + ".web_elements"})
public class WebElementsConfig {
    private final WebDriversProperties webDriversProperties;

    /**
     * Sets browser reusing property
     */
    @EventListener(classes = {ContextRefreshedEvent.class})
    public void configureOnStartUp() {
        System.setProperty(
                Constants.ENABLE_BROWSER_REUSE, String.valueOf(webDriversProperties.isReuseBrowserEnabled()));
    }
}
