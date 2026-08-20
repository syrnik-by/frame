package ru.autotestframework.core;

import io.github.resilience4j.retry.RetryConfig;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.step_executor.StepExecutorImpl;
import ru.autotestframework.cucumber.step_executor.StepRunnable;

public class StepExecutorImplTests {

    @Test
    void Enable1Test() {
        StepExecutorImpl stepExecutor = new StepExecutorImpl();
        stepExecutor.enable(1, 2);
        RetryConfig retryConfig = (RetryConfig) ReflectionTestUtils.getField(stepExecutor, "retryConfig");
        Assertions.assertEquals(1, retryConfig.getMaxAttempts());
    }

    @Test
    void Enable2Test() {
        StepExecutorImpl stepExecutor = new StepExecutorImpl();
        stepExecutor.enable(3);
        Integer minPassedSteps = (Integer) ReflectionTestUtils.getField(stepExecutor, "minPassedSteps");
        Boolean enabled = (Boolean) ReflectionTestUtils.getField(stepExecutor, "enabled");
        Assertions.assertEquals(3, minPassedSteps);
        Assertions.assertTrue(enabled);
    }

    @Test
    void isEnabledTest() {
        StepExecutorImpl stepExecutor = new StepExecutorImpl();
        Assertions.assertFalse(stepExecutor.isEnabled());
        ReflectionTestUtils.setField(stepExecutor, "enabled", true);
        Assertions.assertTrue(stepExecutor.isEnabled());
    }

    @Test
    void addStepDisabledTest() {
        StepRunnable stepRunnable = Mockito.mock(StepRunnable.class);
        StepExecutorImpl stepExecutor = new StepExecutorImpl();
        Assertions.assertThrows(AutotestException.class, () -> stepExecutor.addStep(stepRunnable));
    }

    @Test
    void addStepEnabledTest() {
        StepRunnable stepRunnable = Mockito.mock(StepRunnable.class);
        StepExecutorImpl stepExecutor = new StepExecutorImpl();
        ReflectionTestUtils.setField(stepExecutor, "enabled", true);
        stepExecutor.addStep(stepRunnable);
        List<StepRunnable> steps = (List<StepRunnable>) ReflectionTestUtils.getField(stepExecutor, "steps");
        Assertions.assertEquals(stepRunnable, steps.get(0));
    }
}
