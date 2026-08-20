package ru.autotestframework.test_scope_info;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "framework")
public class StepInfoProperties {

    @Value("${framework.step.metainfo.enabled:false}")
    private boolean stepMetaInfoEnabled;
}
