package ru.autotestframework.core.exception;

/**
 * An exception that is thrown after the timeout has expired.
 */
public class TimeoutException extends AutotestException {

    /**
     * Instantiates a new Timeout exception.
     *
     * @param message the message
     */
    public TimeoutException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Timeout exception.
     *
     * @param message the message
     * @param args    the args
     */
    public TimeoutException(final String message, final Object... args) {
        super(message, args);
    }
}
