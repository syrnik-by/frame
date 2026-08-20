package ru.autotestframework;

import static ru.autotestframework.Constants.ALLURE_PLUGIN;
import static ru.autotestframework.Constants.DEFAULT_FEATURES;
import static ru.autotestframework.Constants.DEFAULT_GLUE;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = DEFAULT_FEATURES,
        tags = "@desk",
        glue = DEFAULT_GLUE,
        plugin = {"pretty", ALLURE_PLUGIN})
public class CucumberRunner {}
