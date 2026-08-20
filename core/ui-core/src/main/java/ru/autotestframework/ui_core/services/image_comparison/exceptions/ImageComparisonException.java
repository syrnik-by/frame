package ru.autotestframework.ui_core.services.image_comparison.exceptions;

import ru.autotestframework.core.exception.AutotestException;

/**
 * A {@link AutotestException} that is thrown in case of an image comparison failures.
 */
public class ImageComparisonException extends AutotestException {

    /**
     * Constructs a new {@link ImageComparisonException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public ImageComparisonException(final String message) {
        super(message);
    }

    /**
     * Constructs a new {@link ImageComparisonException} with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public ImageComparisonException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
