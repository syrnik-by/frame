package ru.autotestframework.core.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Application start context listener.
 */
@Slf4j
@Component
public class ApplicationStartContextListener implements ApplicationListener<FeatureStartEvent> {
    @Override
    public void onApplicationEvent(FeatureStartEvent event) {
        SpringTestContext.getInstance().start();
    }
}
