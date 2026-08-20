package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class Spinner extends TypifiedDesktopElement {

    private static final String SPINNER_MINIMUM = "spinnerMinimum";
    private static final String SPINNER_MAXIMUM = "spinnerMaximum";
    private static final String SPINNER_SMALL_CHANGE = "spinnerSmallChange";
    private static final String SPINNER_IS_ONLY_VALUE = "spinnerIsOnlyValue";
    private static final String SPINNER_SET_VALUE = "spinnerSetValue";
    private static final String SPINNER_INCREMENT = "spinnerIncrement";
    private static final String SPINNER_DECREMENT = "spinnerDecrement";

    public Spinner(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * The minimum value.
     *
     * @return minimum value of the spinner
     */
    public double minimum() {
        var response = callVoidCommand(SPINNER_MINIMUM);
        return Double.parseDouble(response.getValue().toString());
    }

    /**
     * The maximum value.
     *
     * @return maximum value of the spinner
     */
    public double maximum() {
        var response = callVoidCommand(SPINNER_MAXIMUM);
        return Double.parseDouble(response.getValue().toString());
    }

    /**
     * The value of a small change.
     *
     * @return small change value of the spinner
     */
    public double smallChange() {
        var response = callVoidCommand(SPINNER_SMALL_CHANGE);
        return Double.parseDouble(response.getValue().toString());
    }

    /**
     * Flag which indicates if the "Spinner" supports range values (min-max) or only values (0-100).
     * Only values are for example used when combining UIA3 and WinForms applications.
     *
     * @return whether the spinner supports range values or only values
     */
    public boolean isOnlyValue() {
        var response = callVoidCommand(SPINNER_IS_ONLY_VALUE);
        return Boolean.parseBoolean(response.getValue().toString());
    }

    /**
     * Gets the current value.
     *
     * @return current value of the spinner
     */
    public double getValue() {
        String response = getSelenideElement().getText();
        return Double.parseDouble(response);
    }

    /**
     * Sets the current value.
     *
     * @param value specified value that should be set to the spinner
     */
    public void setValue(final double value) {
        callValueCommand(SPINNER_SET_VALUE, String.valueOf(value));
    }

    /**
     * Performs increment.
     */
    public void increment() {
        callVoidCommand(SPINNER_INCREMENT);
    }

    /**
     * Performs decrement.
     */
    public void decrement() {
        callVoidCommand(SPINNER_DECREMENT);
    }
}
