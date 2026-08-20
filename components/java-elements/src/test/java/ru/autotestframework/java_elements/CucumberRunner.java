package ru.autotestframework.java_elements;

import static ru.autotestframework.Constants.*;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = DEFAULT_FEATURES,
        tags = DEFAULT_TAGS,
        glue = DEFAULT_GLUE,
        plugin = {"pretty", ALLURE_PLUGIN})
public class CucumberRunner {}
