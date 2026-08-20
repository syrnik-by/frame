package ru.autotestframework.cucumber.core.descriptions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Parameters {
    /**
     * Value parameter [ ].
     *
     * @return the parameter [ ]
     */
    Parameter[] value();
}
