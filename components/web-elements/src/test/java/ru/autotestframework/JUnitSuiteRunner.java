package ru.autotestframework;

import static io.cucumber.junit.platform.engine.Constants.JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.ConfigurationParameters;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SuiteDisplayName;

// @Suite
@SuiteDisplayName("JUnit Platform Suite Demo")
@IncludeTags("@junit")
// @SelectPackages("ru.autotestframework.junit.tests")
@ConfigurationParameters({
    @ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long"),
})
public class JUnitSuiteRunner {}
