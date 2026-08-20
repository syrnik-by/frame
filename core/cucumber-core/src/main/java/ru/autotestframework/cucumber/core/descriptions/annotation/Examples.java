package ru.autotestframework.cucumber.core.descriptions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Examples for steps.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Examples {
    /**
     * Value example [ ].
     *
     * @return list of Cucumber Step Examples.
     */
    Example[] value();
}
