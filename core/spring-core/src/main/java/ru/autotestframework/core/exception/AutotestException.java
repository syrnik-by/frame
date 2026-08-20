package ru.autotestframework.core.exception;

import ru.autotestframework.util.StringUtil;

/**
 * The parent exception that is thrown for any problems in the utility classes of the Core framework.
 * Can be used in cases of wrapping up any difficult-to-classify exceptions.
 */
public class AutotestException extends RuntimeException {

    /**
     * Instantiates a new Autotest exception.
     *
     * @param message the message
     * @param args    the args
     */
    public AutotestException(final String message, final Object... args) {
        super(StringUtil.format(message, args));
    }

    /**
     * Instantiates a new Autotest exception.
     *
     * @param message the message
     * @param cause   the cause
     * @param args    the args
     */
    public AutotestException(final String message, final Throwable cause, final Object... args) {
        super(StringUtil.format(message, args), cause);
    }
}
