package ru.autotestframework.ui_core.typified_elements.ifaces;

import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

/**
 * An interface for creating typed elements that can be cleaned.
 */
public interface ICleanable {

    /**
     * Clean.
     */
    default void clean() {
        throw new ElementInteractionException(
                "'clean' function not implemented for type '{}'", getClass().getSimpleName());
    }
}
