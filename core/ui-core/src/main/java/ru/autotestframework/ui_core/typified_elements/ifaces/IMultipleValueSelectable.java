package ru.autotestframework.ui_core.typified_elements.ifaces;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import ru.autotestframework.ui_core.UiCoreUtils;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.enums.FixState;

/**
 * An interface for creating typed elements that allow you to select values in an element/list.
 */
public interface IMultipleValueSelectable extends IMultipleValueTypeable, ISelectable {

    /**
     * Select multiple.
     *
     * @param values the values
     * @throws ElementInteractionException the element interaction exception
     */
    default void selectMultiple(Collection<?> values) throws ElementInteractionException {
        throw new ElementInteractionException(
                "'selectMultiple' function not implemented for type '{}'",
                getClass().getSimpleName());
    }

    @Override
    default void select(String value) throws ElementInteractionException {
        if (isFixStateValue()) {
            List<FixState> valueList = Arrays.stream(value.split(getStringArrayDelimiter()))
                    .map(String::trim)
                    .map(FixState::determine)
                    .collect(Collectors.toList());
            selectMultiple(valueList);
        } else {
            List<String> valueList = UiCoreUtils.parseValueList(value, getStringArrayDelimiter());
            selectMultiple(valueList);
        }
    }
}
