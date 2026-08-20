package ru.autotestframework.ui_core.typified_elements.ifaces;

import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;

/**
 * An interface for creating typed elements that require filling.
 */
public interface IWritable extends IValueTypeable, IVerifiable {

    /**
     * Write.
     *
     * @param value the value
     * @throws ElementInteractionException the element interaction exception
     */
    default void write(String value) throws ElementInteractionException {
        throw new ElementInteractionException(
                "'write' function not implemented for type '{}'", getClass().getSimpleName());
    }

    /**
     * Append.
     *
     * @param value the value
     * @throws ElementInteractionException the element interaction exception
     */
    default void append(String value) throws ElementInteractionException {
        throw new ElementInteractionException(
                "'append' function not implemented for type '{}'", getClass().getSimpleName());
    }

    /**
     * Write fix state.
     *
     * @param value the value
     * @throws ElementInteractionException the element interaction exception
     */
    @Deprecated
    default void writeFixState(FixState value) throws ElementInteractionException {
        throw new ElementInteractionException(
                "'writeBoolean' function not implemented for type '{}'",
                getClass().getSimpleName());
    }

    /**
     * Is editable boolean.
     *
     * @return the boolean
     */
    default boolean isEditable() {
        throw new ElementInteractionException(
                "'isEditable' function not implemented for type '{}'",
                getClass().getSimpleName());
    }
}
