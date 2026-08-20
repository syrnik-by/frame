package ru.autotestframework.sql_steps;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import ru.autotestframework.Constants;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = Constants.DEFAULT_FEATURES,
        tags = "@SQLDemo",
        glue = Constants.DEFAULT_GLUE,
        plugin = {"pretty", Constants.ALLURE_PLUGIN})
public class CucumberRunner {}
