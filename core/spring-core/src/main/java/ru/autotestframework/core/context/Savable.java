package ru.autotestframework.core.context;

/**
 * {@code Savable} represents an object that will have the {@code save()} method for saving context.
 */
public interface Savable {

    /**
     * Performs the actions necessary to save the context.
     */
    void save();
}
