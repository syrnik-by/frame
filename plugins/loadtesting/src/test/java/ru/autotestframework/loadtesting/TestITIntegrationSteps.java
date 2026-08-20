package ru.autotestframework.loadtesting;

import io.cucumber.java.en.When;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;

public class TestITIntegrationSteps {
    @When("✽ Шаг номер {word} ✽")
    @Sample("Обозначает номер шага в сценарии в Test IT")
    @Parameter(type = "{word}", name = "номер шага в Test IT")
    @Example(example = "* ✽ Шаг номер 1 ✽")
    @TestITLoadTestingStep
    public void markStepNumber(String stepNumber) {}
}
