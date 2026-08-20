package ru.autotestframework.ui_core.services.image_comparison.exceptions;

import ru.autotestframework.core.exception.AutotestException;

/**
 * {@link AutotestException} that is thrown in case of an image getting failures.
 */
public class ImageNotFoundException extends AutotestException {

    /**
     * Constructs a new {@link ImageNotFoundException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public ImageNotFoundException(final String message) {
        super(message);
    }
}
