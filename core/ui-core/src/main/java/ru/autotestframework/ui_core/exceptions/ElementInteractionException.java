package ru.autotestframework.ui_core.exceptions;

import static ru.autotestframework.util.StringUtil.format;

import ru.autotestframework.core.exception.AutotestException;

/**
 * An exception that is thrown for any problems when interacting with UI elements.
 */
public class ElementInteractionException extends AutotestException {

    /**
     * Instantiates a new Element interaction exception.
     *
     * @param message the message
     */
    public ElementInteractionException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Element interaction exception.
     *
     * @param message the message
     * @param args    the args
     */
    public ElementInteractionException(final String message, final Object... args) {
        super(message, args);
    }

    /**
     * Instantiates a new Element interaction exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public ElementInteractionException(final String message, final Throwable cause, final Object... args) {
        super(format(message, args), cause);
    }
}
