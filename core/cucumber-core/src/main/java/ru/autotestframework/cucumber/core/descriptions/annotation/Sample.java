package ru.autotestframework.cucumber.core.descriptions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sample.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Sample {
    /**
     * Value string.
     *
     * @return the string
     */
    String value() default "";
}
