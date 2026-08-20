package ru.autotestframework;

import static ru.autotestframework.Constants.ALLURE_PLUGIN;
import static ru.autotestframework.Constants.DEFAULT_FEATURES;
import static ru.autotestframework.Constants.DEFAULT_GLUE;
import static ru.autotestframework.Constants.DEFAULT_TAGS;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Cucumber runner.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = DEFAULT_FEATURES,
        tags = DEFAULT_TAGS,
        glue = DEFAULT_GLUE,
        plugin = {"pretty", ALLURE_PLUGIN})
public class CucumberRunner {}
