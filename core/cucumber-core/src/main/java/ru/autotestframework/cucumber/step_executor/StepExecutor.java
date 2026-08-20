package ru.autotestframework.cucumber.step_executor;

import ru.autotestframework.core.context.Cleanable;

/**
 * StepExecutor.
 * <p>
 * A class that allows you not to execute steps at once, but to put them in a chain,
 * and then execute the entire chain of steps with a certain number of attempts.
 */
public interface StepExecutor extends Cleanable {

    /**
     * Enables the collection of steps in a chain.
     *
     * @param maxAttempts  maximum number of execution attempts
     * @param waitDuration the waiting interval between attempts, specified in milliseconds
     */
    void enable(Integer maxAttempts, Integer waitDuration);

    /**
     * Enables the collection of steps in a chain.
     *
     * @param minPassedSteps - the minimum number of steps that must be completed successfully
     */
    void enable(Integer minPassedSteps);

    /**
     * Is enabled boolean.
     *
     * @return true - step collection is enabled <br> false - step collection is disabled
     */
    boolean isEnabled();

    /**
     * If step collection is enabled, it adds a step to the chain.
     * <br>
     * If step collection is disabled, it throws {@code AutotestException}
     *
     * @param step A step wrapped in a Runnable
     */
    void addStep(StepRunnable step);

    /**
     * Performs the entire chain of steps, and disables the collection of steps.
     */
    void execute();
}
