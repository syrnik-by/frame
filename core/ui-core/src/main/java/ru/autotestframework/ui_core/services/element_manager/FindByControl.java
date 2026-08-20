package ru.autotestframework.ui_core.services.element_manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Find by control.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface FindByControl {

    /**
     * Win title string.
     *
     * @return the string
     */
    String winTitle() default "";

    /**
     * Control string.
     *
     * @return the string
     */
    String control();
}
