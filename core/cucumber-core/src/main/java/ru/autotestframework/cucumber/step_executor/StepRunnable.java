package ru.autotestframework.cucumber.step_executor;

/**
 * Step runnable.
 */
public interface StepRunnable extends Runnable {

    /**
     * Gets step expression.
     *
     * @return the step expression
     */
    String getStepExpression();

    /**
     * Sets args.
     *
     * @param args the args
     * @return the args
     */
    StepRunnable setArgs(Object[] args);
}
