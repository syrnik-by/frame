package ru.autotestframework.ui_core.typified_elements.ifaces;

import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

/**
 * Scrollable.
 */
public interface IScrollable {

    /**
     * Scroll.
     *
     * @param offset the offset
     * @throws ElementInteractionException the element interaction exception
     */
    default void scroll(Integer offset) throws ElementInteractionException {
        throw new ElementInteractionException(
                "'scroll' function not implemented for type '{}'", getClass().getSimpleName());
    }
}
