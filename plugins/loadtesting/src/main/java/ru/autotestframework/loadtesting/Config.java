package ru.autotestframework.loadtesting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.autotestframework.configuration.ProxyProperties;
import ru.proxy.ProxyContainer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class Config {
    private final ProxyProperties properties;

    @Bean
    public ProxyContainer proxyContainer() {
        if (properties.isEnabled()) {
            ProxyContainer.getInstance().init(properties.getHost(), properties.getPort());
            return ProxyContainer.getInstance();
        }
        return null;
    }
}
