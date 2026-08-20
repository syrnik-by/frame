package ru.autotestframework.ui_core.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Ui properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "framework.ui")
public class UiProperties {
    @Value("${framework.ui.page.package:ru.autotestframework.pages}")
    private String[] pagePackage;

    @Value("${framework.ui.timeout:4}")
    private int timeout;

    @Value("${framework.ui.driver.driverInit:true}")
    private Boolean driverInit;

    @Value("${framework.ui.aspects.highlight.enabled:false}")
    private boolean highlightEnabled;

    @Value("${framework.ui.aspects.tableCache.enabled:false}")
    private boolean tableCacheEnabled;

    @Value("${framework.ui.allure.screenShootingOnCore.enabled:false}")
    private boolean screenShootingOnCoreActionsEnabled;

    @Value("${framework.ui.allure.screenShootingOnAnnotation.enabled:false}")
    private boolean screenShootingOnAnnotationEnabled;

    @Value("${framework.ui.imageComparison.pixelToleranceLevel:0.1}")
    private double pixelToleranceLevel;

    @Value("${framework.ui.imageComparison.allowingPercentOfDifferentPixels:0.0}")
    private double allowingPercentOfDifferentPixels;

    @Value("${framework.ui.closeOnFail:true}")
    private boolean closeOnFail;

    @Value("${framework.ui.start:true}")
    private boolean startUI;
}
