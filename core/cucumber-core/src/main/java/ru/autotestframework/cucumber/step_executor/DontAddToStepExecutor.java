package ru.autotestframework.cucumber.step_executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is used to mark Cucumber steps,
 * which should not be added to StepExecutor.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DontAddToStepExecutor {}
