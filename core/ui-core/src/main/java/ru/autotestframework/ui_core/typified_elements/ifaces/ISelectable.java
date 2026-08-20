package ru.autotestframework.ui_core.typified_elements.ifaces;

import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;

/**
 * An interface for creating typed elements that allow you to select values in an element/list.
 */
public interface ISelectable extends IValueTypeable, IVerifiable {

    /**
     * Select.
     *
     * @param value the value
     * @throws ElementInteractionException the element interaction exception
     */
    default void select(FixState value) throws ElementInteractionException {
        throw new ElementInteractionException(
                "'select' function not implemented for type '{}'", getClass().getSimpleName());
    }

    /**
     * Select.
     *
     * @param value the value
     * @throws ElementInteractionException the element interaction exception
     */
    default void select(String value) throws ElementInteractionException {
        throw new ElementInteractionException(
                "'select' function not implemented for type '{}'", getClass().getSimpleName());
    }
}
