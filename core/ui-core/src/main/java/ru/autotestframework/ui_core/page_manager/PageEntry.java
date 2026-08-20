package ru.autotestframework.ui_core.page_manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation is intended to indicate the name of the page. The name must be unique
 * for the entire project. Only those pages that have this annotation will be included in the page manager.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PageEntry {

    /**
     * Title string.
     *
     * @return Именование Страницы.
     */
    String title();
}
