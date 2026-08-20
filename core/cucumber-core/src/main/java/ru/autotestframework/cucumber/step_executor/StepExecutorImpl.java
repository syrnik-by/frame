package ru.autotestframework.cucumber.step_executor;

import static ru.autotestframework.Messages.RETRYABLE_STEPS_EXAMPLE;
import static ru.autotestframework.util.Validator.exception;

import io.cucumber.spring.ScenarioScope;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

/**
 * Step executor.
 */
@Slf4j
@Component
@ScenarioScope
public class StepExecutorImpl implements StepExecutor {

    private final List<StepRunnable> steps = new ArrayList<>();

    private final AtomicInteger attemptCount = new AtomicInteger(0);
    private final List<StepRunnable> passedSteps = new ArrayList<>();
    private final List<Pair<StepRunnable, String>> failedSteps = new ArrayList<>();

    private boolean enabled = false;
    private RetryConfig retryConfig;
    private Integer minPassedSteps;

    @Override
    public void enable(final Integer maxAttempts, final Integer waitDuration) {
        if (!enabled) {
            resetState();
        }
        retryConfig = RetryConfig.custom()
                .maxAttempts(maxAttempts)
                .waitDuration(Duration.ofMillis(waitDuration))
                .build();
        enabled = true;
    }

    @Override
    public void enable(final Integer minPassedSteps) {
        if (!enabled) {
            resetState();
        }
        this.minPassedSteps = minPassedSteps;
        enabled = true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void addStep(final StepRunnable step) {
        if (enabled) {
            steps.add(step);
        } else {
            throw exception("Impossible to add Step to StepExecutor, because it's disabled");
        }
    }

    @Override
    @SneakyThrows
    public void execute() {
        enabled = false;
        if (useRetryLogic() && !shouldCountPassedSteps()) {
            executeWithRetries(() -> steps.forEach(Runnable::run));
            log.info("Chain of {} Steps was completed. Attempts count: {}", steps.size(), attemptCount);
        } else if (shouldCountPassedSteps() && !useRetryLogic()) {
            executeWithCountingPassedSteps();
            log.info(
                    "Chain of Steps was completed. Successfully passed {} Step(s) out of {}",
                    passedSteps.size(),
                    steps.size());
        } else {
            executeWithRetries(this::executeWithCountingPassedSteps);
            log.info(
                    "Chain of Steps was completed. Successfully passed {} Step(s) out of {}. "
                            + "Steps were passed within {} attempts",
                    passedSteps.size(),
                    steps.size(),
                    attemptCount);
        }
    }

    private boolean useRetryLogic() {
        return retryConfig != null;
    }

    private boolean shouldCountPassedSteps() {
        return minPassedSteps != null;
    }

    private void executeWithRetries(final Runnable runnable) throws Exception {
        var retry = Retry.of("retry", retryConfig);
        var context = retry.context();
        do {
            try {
                attemptCount.incrementAndGet();
                runnable.run();
                context.onComplete();
                break;
            } catch (Throwable e) {
                if (attemptCount.get() == retryConfig.getMaxAttempts()) {
                    throw e;
                }
                context.onError(new Exception(e));
            }
        } while (true);
    }

    private void executeWithCountingPassedSteps() {
        passedSteps.clear();
        failedSteps.clear();
        for (var step : steps) {
            try {
                step.run();
                passedSteps.add(step);
                if (passedSteps.size() >= minPassedSteps) {
                    return;
                }
            } catch (Exception exception) {
                failedSteps.add(Pair.of(step, exception.getMessage()));
                if (steps.size() - failedSteps.size() < minPassedSteps) {
                    var errorsDescription = buildErrorsDescription(failedSteps);
                    throw exception(
                            "Error of Steps Chain executing. {} step(s) out of {} failed with error.\n{}",
                            failedSteps.size(),
                            steps.size(),
                            errorsDescription);
                }
            }
        }
    }

    private String buildErrorsDescription(final List<Pair<StepRunnable, String>> failedSteps) {
        var joiner = new StringJoiner("\n", "List of Steps with errors: \n", "\n");
        for (var pair : failedSteps) {
            var stepExpression = pair.getLeft().getStepExpression();
            var errorMessage = pair.getRight();
            joiner.add("Step '" + stepExpression + "' was failed with Error '" + errorMessage + "'");
        }
        return joiner.toString();
    }

    private void resetState() {
        steps.clear();
        retryConfig = null;
        minPassedSteps = null;
        attemptCount.set(0);
        passedSteps.clear();
        failedSteps.clear();
    }

    @Override
    public void clean() {
        validateThatExecuted();
    }

    private void validateThatExecuted() {
        if (enabled) {
            throw exception("Incorrect completion of Retry Steps Chain. {}", RETRYABLE_STEPS_EXAMPLE);
        }
    }
}
