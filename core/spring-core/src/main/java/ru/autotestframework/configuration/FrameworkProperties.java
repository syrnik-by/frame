package ru.autotestframework.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Framework properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "framework")
public class FrameworkProperties {

    @Value("${framework.ssl.enabled:false}")
    private boolean sslEnabled;

    @Value("${framework.ssl.trust-store:}")
    private String trustStore;

    @Value("${framework.deprecated-features.enabled:false}")
    private boolean deprecatedFeaturesEnabled;

    @Value("${framework.remove.git.hooks:true}")
    private String removeGitHooks;

    @Value("${framework.temp.files.cleaning.enabled:true}")
    private Boolean tempFilesCleaningEnabled;

    @Value("${framework.fake.db.driver.use:}")
    private String fakeDbDriverUse;

    @Value(value = "${framework.decimal.comparison.scale:4}")
    private String decimalPrecisionScale;

    @Value(value = "${framework.array.string.delimiter:; }")
    private String arrayStringDelimiter;

    @Value("${framework.access.check.enabled:false}")
    private boolean accessCheckEnabled;

    @Value("${framework.parallelization.enabled:false}")
    private boolean parallelizationEnabled;

    @Value("${framework.unmasking.enabled:false}")
    private boolean unmaskingVariablesEnabled;
}
