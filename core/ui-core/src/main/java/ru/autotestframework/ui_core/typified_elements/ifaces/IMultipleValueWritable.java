package ru.autotestframework.ui_core.typified_elements.ifaces;

import java.util.Collection;
import java.util.List;
import ru.autotestframework.ui_core.UiCoreUtils;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

/**
 * An interface for creating typed elements that require filling.
 */
public interface IMultipleValueWritable extends IWritable, IMultipleValueTypeable {

    @Override
    default void write(String value) throws ElementInteractionException {
        List<String> valueList = UiCoreUtils.parseValueList(value, getStringArrayDelimiter());
        writeMultiple(valueList);
    }

    @Override
    default void append(String value) throws ElementInteractionException {
        List<String> valueList = UiCoreUtils.parseValueList(value, getStringArrayDelimiter());
        appendMultiple(valueList);
    }

    /**
     * Append multiple.
     *
     * @param values the values
     * @throws ElementInteractionException the element interaction exception
     */
    default void appendMultiple(Collection<String> values) throws ElementInteractionException {
        throw new ElementInteractionException(
                "'appendMultiple' function not implemented for type '{}'",
                getClass().getSimpleName());
    }

    /**
     * Write multiple.
     *
     * @param values the values
     * @throws ElementInteractionException the element interaction exception
     */
    default void writeMultiple(Collection<String> values) throws ElementInteractionException {
        throw new ElementInteractionException(
                "'writeMultiple' function not implemented for type '{}'",
                getClass().getSimpleName());
    }
}
