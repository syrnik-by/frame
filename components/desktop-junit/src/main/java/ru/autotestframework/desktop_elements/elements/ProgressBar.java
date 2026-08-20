package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class ProgressBar extends TypifiedDesktopElement {

    private static final String PROGRESS_BAR_MINIMUM = "progressBarMinimum";
    private static final String PROGRESS_BAR_MAXIMUM = "progressBarMaximum";
    private static final String PROGRESS_BAR_VALUE = "progressBarValue";

    public ProgressBar(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * Gets the minimum value.
     *
     * @return minimum value of the progress bar
     */
    public double minimum() {
        var response = callVoidCommand(PROGRESS_BAR_MINIMUM);
        return parseDouble(response);
    }

    /**
     * Gets the maximum value.
     *
     * @return maximum value of the progress bar
     */
    public double maximum() {
        var response = callVoidCommand(PROGRESS_BAR_MAXIMUM);
        return parseDouble(response);
    }

    /**
     * Gets the current value.
     *
     * @return current value of the progress bar
     */
    public double value() {
        var response = callVoidCommand(PROGRESS_BAR_VALUE);
        return parseDouble(response);
    }
}
