package ru.autotestframework.test_scope_info;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.spring.ScenarioScope;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Cucumber Hooks to save information about Scenario and Steps
 */
@ScenarioScope
@RequiredArgsConstructor
public class StepInfoHooks {

    private final StepInfoContainer stepInfoContainer;
    private final StepInfoProperties frameworkProperties;

    private int currentStepDefIndex = 0;
    private List<PickleStepTestStep> stepDefs;

    @Before(order = Integer.MAX_VALUE)
    public void setup(final Scenario scenario) throws Exception {
        if (frameworkProperties.isStepMetaInfoEnabled()) {
            var delegateField = scenario.getClass().getDeclaredField("delegate");
            delegateField.setAccessible(true);
            TestCaseState tcState = (TestCaseState) delegateField.get(scenario);

            var r = getTestCase(tcState);

            stepDefs = r.getTestSteps().stream()
                    .filter(step -> step instanceof PickleStepTestStep)
                    .map(step -> (PickleStepTestStep) step)
                    .collect(Collectors.toList());
            stepInfoContainer.setScenarioName(scenario.getName());

            stepInfoContainer.setScenarioTags(new ArrayList(scenario.getSourceTagNames()));
        }
    }

    public TestCase getTestCase(TestCaseState tcState) throws NoSuchFieldException, IllegalAccessException {
        var testCaseField = tcState.getClass().getDeclaredField("testCase");
        testCaseField.setAccessible(true);
        return (TestCase) testCaseField.get(tcState);
    }

    @SneakyThrows
    @BeforeStep
    public void setCurrentStepInfo() {
        if (frameworkProperties.isStepMetaInfoEnabled()) {
            PickleStepTestStep currentStepDef = stepDefs.get(currentStepDefIndex);
            stepInfoContainer.setCurrentStep(currentStepDef.getStep());
            currentStepDefIndex++;
        }
    }
}
