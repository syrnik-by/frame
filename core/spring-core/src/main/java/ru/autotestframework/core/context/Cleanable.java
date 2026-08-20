package ru.autotestframework.core.context;

/**
 * {@code Cleanable} represents an object that will have the {@code clean()} method called during context cleanup.
 */
public interface Cleanable {

    /**
     * Performs the actions necessary to clear the context.
     */
    void clean();
}
