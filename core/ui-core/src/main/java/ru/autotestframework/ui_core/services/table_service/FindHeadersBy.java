package ru.autotestframework.ui_core.services.table_service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * By Annotation to Find Table Headers Elements within UI.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface FindHeadersBy {
    /**
     * Xpath string.
     *
     * @return the string
     */
    String xpath() default "";
}
