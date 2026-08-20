package ru.autotestframework.cucumber.core.descriptions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Example for steps.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Examples.class)
public @interface Example {
    /**
     * Number string.
     *
     * @return the string
     */
    String number() default "";

    /**
     * Example string.
     *
     * @return the string
     */
    String example();
}
