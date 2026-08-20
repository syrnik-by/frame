package ru.autotestframework;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

// Old cucumber junit4 runner
@RunWith(Cucumber.class)
@CucumberOptions(
        features = Constants.DEFAULT_FEATURES,
        tags = Constants.DEFAULT_TAGS,
        glue = Constants.DEFAULT_GLUE,
        plugin = {"pretty", Constants.ALLURE_PLUGIN})
public class CucumberRunner {}
