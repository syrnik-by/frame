package ru.autotestframework.web_elements.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "framework.ui.driver.web")
public class WebDriversProperties {

    @Value("${framework.ui.driver.web.path:}")
    private String path;

    @Value("${framework.ui.driver.web.repo.url:}")
    private String repoUrl;

    @Value("${framework.ui.driver.web.repo.user:}")
    private String repoUser;

    @Value("${framework.ui.driver.web.repo.pass:}")
    private String repoPass;

    @Value("${framework.ui.driver.web.version:}")
    private String version;

    @Value("${framework.ui.driver.web.cache.clear:true}")
    private Boolean cacheClear;

    @Value("${framework.ui.driver.web.headless:false}")
    private boolean headless;

    @Value(value = "${framework.ui.driver.web.starting.url:about:blank}")
    private String startingUrl;

    @Value("${framework.ui.driver.web.properties.path:selenide.properties}")
    private String propertiesPath;

    @Value("${framework.ui.driver.web.reuseBrowserEnabled:false}")
    private boolean reuseBrowserEnabled;

    @Value("${framework.ui.driver.web.browser.logging.strategy:off}")
    private String browserLoggingStrategy;
}
