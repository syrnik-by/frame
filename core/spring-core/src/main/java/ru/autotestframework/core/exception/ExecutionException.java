package ru.autotestframework.core.exception;

import static ru.autotestframework.util.StringUtil.format;

/**
 * An exception that is thrown in case of any problems when interacting with the Backend.
 */
public class ExecutionException extends AutotestException {

    /**
     * Instantiates a new Execution exception.
     *
     * @param message the message
     */
    public ExecutionException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Execution exception.
     *
     * @param message the message
     * @param args    the args
     */
    public ExecutionException(final String message, final Object... args) {
        super(message, args);
    }

    /**
     * Instantiates a new Execution exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public ExecutionException(final String message, final Throwable cause, final Object... args) {
        super(format(message, args), cause);
    }
}
