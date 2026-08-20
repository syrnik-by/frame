package ru.autotestframework.cucumber.type.resolvable;

/**
 * Interface for creating types with automatic resolving under the hood.
 *
 * @param <T> - the data object to be converted to
 */
public interface IResolvable<T> {

    /**
     * Resolve t.
     *
     * @param object the object
     * @return the t
     */
    T resolve(T object);
}
