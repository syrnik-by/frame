package ru.autotestframework.core.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Application stop context listener.
 */
@Component
@Slf4j
public class ApplicationStopContextListener implements ApplicationListener<FeatureEndEvent> {
    @Override
    public void onApplicationEvent(FeatureEndEvent event) {
        SpringTestContext.getInstance().stop();
    }
}
