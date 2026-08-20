package ru.autotestframework.ui_core.typified_elements.ifaces;

import java.util.Collection;
import java.util.stream.Collectors;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;

/**
 * An interface for creating typed elements from which data can be read.
 */
public interface IMutlipleValueReadable extends IReadable, IMultipleValueTypeable {

    /**
     * @return should return the value of the element as a string
     */
    @Override
    default String readValue() {
        return readMultipleValues().stream().collect(Collectors.joining(getStringArrayDelimiter()));
    }

    /**
     * Read multiple values collection.
     *
     * @return should return a list of values
     */
    default Collection<String> readMultipleValues() {
        throw new ElementInteractionException(
                "'readMultipleValues' function not implemented for type '{}'",
                getClass().getSimpleName());
    }

    /**
     * Read multiple states collection.
     *
     * @return should return a list of enum {@link FixState} values
     */
    @Deprecated
    default Collection<FixState> readMultipleStates() {
        throw new ElementInteractionException(
                "'readMultipleStates()' function not implemented for type '{}'",
                getClass().getSimpleName());
    }
}
