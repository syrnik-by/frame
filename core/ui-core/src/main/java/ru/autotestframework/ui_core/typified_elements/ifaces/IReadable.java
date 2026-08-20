package ru.autotestframework.ui_core.typified_elements.ifaces;

import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;

/**
 * An interface for creating typed elements from which data can be read.
 */
public interface IReadable extends IValueTypeable {

    /**
     * Read value string.
     *
     * @return must return the value of the element as a string
     */
    default String readValue() {
        throw new ElementInteractionException(
                "'readValue' function not implemented for type '{}'", getClass().getSimpleName());
    }

    /**
     * Read state fix state.
     *
     * @return must return the enum {@link FixState}
     */
    default FixState readState() {
        throw new ElementInteractionException(
                "'readBoolean' function not implemented for type '{}'",
                getClass().getSimpleName());
    }
}
