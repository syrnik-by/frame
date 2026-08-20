package ru.autotestframework.ui_core.exceptions;

import static ru.autotestframework.util.StringUtil.format;

import ru.autotestframework.core.exception.AutotestException;

/**
 * An exception that is thrown in case of any problems during initialization of pages, elements, etc.
 */
public class InitializationException extends AutotestException {

    /**
     * Instantiates a new Initialization exception.
     *
     * @param message the message
     */
    public InitializationException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Initialization exception.
     *
     * @param message the message
     * @param args    the args
     */
    public InitializationException(final String message, final Object... args) {
        super(message, args);
    }

    /**
     * Instantiates a new Initialization exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public InitializationException(final String message, final Throwable cause, final Object... args) {
        super(format(message, args), cause);
    }
}
