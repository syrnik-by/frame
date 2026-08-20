package ru.autotestframework.ui_core.typified_elements;

import ru.autotestframework.ui_core.exceptions.ElementInteractionException;

/**
 * Element data.
 */
@FunctionalInterface
public interface IElementData {
    /**
     * Execute.
     *
     * @throws ElementInteractionException the element interaction exception
     */
    void execute() throws ElementInteractionException;
}
