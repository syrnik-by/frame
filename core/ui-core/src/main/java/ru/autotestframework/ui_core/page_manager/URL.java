package ru.autotestframework.ui_core.page_manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Url.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface URL {
    /**
     * Url string.
     *
     * @return url of Web Page with this element
     */
    String url() default "";
}
