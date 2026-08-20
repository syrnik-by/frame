package ru.autotestframework.debug_steps;

import static ru.autotestframework.Constants.ALLURE_PLUGIN;
import static ru.autotestframework.Constants.DEFAULT_FEATURES;
import static ru.autotestframework.Constants.DEFAULT_GLUE;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Cucumber runner.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = DEFAULT_FEATURES,
        tags = "@DebugDemo",
        glue = DEFAULT_GLUE,
        plugin = {"pretty", ALLURE_PLUGIN})
public class CucumberRunner {}
