package ru.autotestframework.ui_core.typified_elements.ifaces;

import ru.autotestframework.ui_core.typified_elements.enums.FixState;

/**
 * Value typeable.
 */
public interface IValueTypeable {
    /**
     * the method should return true if the element can store multiple values, false for a single value.
     * For example, it should be false for a text string and true for a multi-choice combo box.
     *
     * @return boolean boolean
     */
    default boolean isMultipleValues() {
        return false;
    }

    /**
     * the method must return true if this element assumes storing states.
     * {@link FixState}
     *
     * @return boolean boolean
     */
    boolean isFixStateValue();
}
