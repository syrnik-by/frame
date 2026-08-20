package ru.autotestframework.ui_core.typified_elements.ifaces;

import static ru.autotestframework.Constants.ARRAY_STRING_DELIMETER;

/**
 * Multiple value typeable.
 */
public interface IMultipleValueTypeable extends IValueTypeable {
    /**
     * the method should return true if the element can store multiple values, false for a single value.
     * For example, it should be false for a text string and true for a multi-choice combo box.
     *
     * @return boolean
     */
    @Override
    default boolean isMultipleValues() {
        return true;
    }

    /**
     * Gets string array delimiter.
     *
     * @return the string array delimiter
     */
    default String getStringArrayDelimiter() {
        return ARRAY_STRING_DELIMETER;
    }
}
