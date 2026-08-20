package ru.autotestframework.ui_core;

import java.lang.reflect.Field;

/**
 * Element resolver.
 */
public interface ElementResolver {

    /**
     * Resolve object.
     *
     * @param <T>   the type parameter
     * @param field the field
     * @return the object
     */
    <T> Object resolve(Field field);

    /**
     * Skip apply boolean.
     *
     * @param field the field
     * @return the boolean
     */
    boolean skipApply(Field field);
}
