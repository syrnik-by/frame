package ru.autotestframework.cucumber.core.descriptions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameter.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Parameters.class)
public @interface Parameter {
    /**
     * Type string.
     *
     * @return the string
     */
    String type();

    /**
     * Name string.
     *
     * @return the string
     */
    String name();
}
