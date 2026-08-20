import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Step;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestStep;
import java.util.List;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.test_scope_info.StepInfoContainer;
import ru.autotestframework.test_scope_info.StepInfoHooks;
import ru.autotestframework.test_scope_info.StepInfoProperties;

@Tag("@TestInfo")
class StepInfoHooksTest {

    @Test
    void hooksSetupTest() throws Exception {

        Scenario scenario = Mockito.mock(Scenario.class);
        StepInfoContainer stepInfoContainer2 = new StepInfoContainer();
        StepInfoProperties properties = new StepInfoProperties();
        StepInfoContainer stepInfoContainer = Mockito.spy(stepInfoContainer2);

        properties.setStepMetaInfoEnabled(true);
        StepInfoHooks hooks = Mockito.mock(StepInfoHooks.class);
        TestCase r = Mockito.mock(TestCase.class);
        TestStep step1 = Mockito.mock(TestStep.class);
        TestCaseState scState = Mockito.mock(TestCaseState.class);

        Mockito.when(scenario.getName()).thenReturn(Faker.instance().ancient().hero());
        Mockito.lenient().when(scState.getSourceTagNames()).thenReturn(List.of());
        TestStep step2 = Mockito.mock(TestStep.class);
        Mockito.lenient().when(r.getTestSteps()).thenReturn(List.of(step1, step2));
        ReflectionTestUtils.setField(scenario, "delegate", scState);
        Mockito.when(hooks.getTestCase(scState)).thenReturn(r);
        ReflectionTestUtils.setField(hooks, "frameworkProperties", properties);
        ReflectionTestUtils.setField(hooks, "stepInfoContainer", stepInfoContainer);
        Mockito.doCallRealMethod().when(hooks).setup(scenario);

        hooks.setup(scenario);

        Assertions.assertEquals(scenario.getName(), stepInfoContainer.getScenarioName());
        Assertions.assertNotNull(scenario.getName());
        Assertions.assertEquals(stepInfoContainer.getScenarioTags(), scenario.getSourceTagNames());
    }

    @Test
    void hooksInfoDisabledTest() throws Exception {
        Scenario scenario = Mockito.mock(Scenario.class);
        StepInfoContainer stepInfoContainer2 = new StepInfoContainer();
        StepInfoContainer stepInfoContainer = Mockito.spy(stepInfoContainer2);
        StepInfoProperties properties = new StepInfoProperties();
        properties.setStepMetaInfoEnabled(false);
        StepInfoHooks hooks = Mockito.mock(StepInfoHooks.class);
        TestCaseState scState = Mockito.mock(TestCaseState.class);
        ReflectionTestUtils.setField(scenario, "delegate", scState);
        ReflectionTestUtils.setField(hooks, "frameworkProperties", properties);
        ReflectionTestUtils.setField(hooks, "stepInfoContainer", stepInfoContainer);
        Mockito.doCallRealMethod().when(hooks).setup(scenario);
        hooks.setup(scenario);
        hooks.setCurrentStepInfo();
        Mockito.verifyNoInteractions(stepInfoContainer, scState);
    }

    @Test
    void hooksTestStepInfoTest() throws Exception {

        Scenario scenario = Mockito.mock(Scenario.class);
        StepInfoContainer stepInfoContainer2 = new StepInfoContainer();
        StepInfoProperties properties = new StepInfoProperties();
        StepInfoContainer stepInfoContainer = Mockito.spy(stepInfoContainer2);
        properties.setStepMetaInfoEnabled(true);
        StepInfoHooks hooks = Mockito.mock(StepInfoHooks.class);
        TestCase r = Mockito.mock(TestCase.class);
        TestStep step1 = Mockito.mock(TestStep.class);
        TestCaseState scState = Mockito.mock(TestCaseState.class);
        Mockito.lenient().when(scState.getName()).thenReturn("someString");
        Mockito.lenient().when(scState.getSourceTagNames()).thenReturn(List.of("vara12"));
        TestStep step2 = Mockito.mock(TestStep.class);
        PickleStepTestStep stepee = Mockito.mock(PickleStepTestStep.class);

        Step step = Mockito.mock(Step.class);

        Mockito.when(stepee.getStep()).thenReturn(step);
        Mockito.lenient().when(r.getTestSteps()).thenReturn(List.of(step1, step2, stepee));
        ReflectionTestUtils.setField(scenario, "delegate", scState);
        Mockito.when(hooks.getTestCase(scState)).thenReturn(r);
        ReflectionTestUtils.setField(hooks, "frameworkProperties", properties);
        ReflectionTestUtils.setField(hooks, "stepInfoContainer", stepInfoContainer);
        Mockito.doCallRealMethod().when(hooks).setup(scenario);
        Mockito.doCallRealMethod().when(hooks).setCurrentStepInfo();

        hooks.setup(scenario);
        hooks.setCurrentStepInfo();

        Assertions.assertEquals(stepee.getStep(), stepInfoContainer.getCurrentStep());
        Assertions.assertNotNull(stepee.getStep());
    }

    @Test
    void hooksConstructorTest() {
        StepInfoContainer stepInfoContainer2 = new StepInfoContainer();
        StepInfoProperties properties = new StepInfoProperties();

        Assertions.assertDoesNotThrow(() -> new StepInfoHooks(stepInfoContainer2, properties));
    }
}
