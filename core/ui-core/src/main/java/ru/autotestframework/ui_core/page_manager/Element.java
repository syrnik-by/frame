package ru.autotestframework.ui_core.page_manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Element.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Element {
    /**
     * Value string.
     *
     * @return the string
     */
    String value();
}
