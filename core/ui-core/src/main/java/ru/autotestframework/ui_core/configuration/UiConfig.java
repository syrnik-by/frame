package ru.autotestframework.ui_core.configuration;

import static com.codeborne.selenide.Configuration.timeout;
import static ru.autotestframework.Constants.DEFAULT_GLUE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * The class implements the configuration of the UI core of the framework and the launch of beans.
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {DEFAULT_GLUE + ".ui_core"})
public class UiConfig implements InitializingBean {
    /**
     * The constant MILLIS_IN_SECOND.
     */
    public static final long MILLIS_IN_SECOND = 1000L;

    private final UiProperties configuration;

    @Override
    public void afterPropertiesSet() {
        timeout = configuration.getTimeout() * MILLIS_IN_SECOND;
    }
}
