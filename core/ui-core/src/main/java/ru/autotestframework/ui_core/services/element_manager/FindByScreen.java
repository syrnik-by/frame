package ru.autotestframework.ui_core.services.element_manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Find by screen.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface FindByScreen {

    /**
     * Region location string.
     *
     * @return the string
     */
    String regionLocation() default "";

    /**
     * Location string.
     *
     * @return the string
     */
    String location();

    /**
     * Search type int.
     *
     * @return the int
     */
    int searchType() default 3;

    /**
     * Offset x int.
     *
     * @return the int
     */
    int offsetX() default 0;

    /**
     * Offset y int.
     *
     * @return the int
     */
    int offsetY() default 0;
}
