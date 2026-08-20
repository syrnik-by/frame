package ru.autotestframework;

import static io.cucumber.core.options.Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME;

import org.junit.platform.suite.api.*;

@SelectClasspathResource("features")
@ConfigurationParameters({
    @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = Constants.DEFAULT_GLUE),
    @ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true"),
    @ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long"),
})
@Suite
public class Cucumber7Runner {}
