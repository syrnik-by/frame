package ru.autotestframework.core.exception;

import ru.autotestframework.util.StringUtil;

/**
 * An exception that is thrown in case of any problems with the project configuration.
 */
public class ConfigurationException extends AutotestException {

    /**
     * Instantiates a new Configuration exception.
     *
     * @param message the message
     */
    public ConfigurationException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Configuration exception.
     *
     * @param message the message
     * @param args    the args
     */
    public ConfigurationException(final String message, final String... args) {
        super(message, args);
    }

    /**
     * Instantiates a new Configuration exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public ConfigurationException(final String message, final Throwable cause, final Object... args) {
        super(StringUtil.format(message, args), cause);
    }
}
