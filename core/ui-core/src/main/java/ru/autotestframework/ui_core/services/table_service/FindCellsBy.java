package ru.autotestframework.ui_core.services.table_service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * xpath to find table cells (elements that contains needed text)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface FindCellsBy {
    /**
     * Xpath string.
     *
     * @return the string
     */
    String xpath() default "";
}
