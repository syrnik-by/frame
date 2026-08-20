package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class Slider extends TypifiedDesktopElement {

    private static final String SLIDER_MINIMUM = "sliderMinimum";
    private static final String SLIDER_MAXIMUM = "sliderMaximum";
    private static final String SLIDER_SMALL_CHANGE = "sliderSmallChange";
    private static final String SLIDER_LARGE_CHANGE = "sliderLargeChange";
    private static final String SLIDER_GET_LARGE_INCREASE_BUTTON = "sliderGetLargeIncreaseButton";
    private static final String SLIDER_GET_LARGE_DECREASE_BUTTON = "sliderGetLargeDecreaseButton";
    private static final String SLIDER_IS_ONLY_VALUE = "sliderIsOnlyValue";
    private static final String SLIDER_SET_VALUE = "sliderSetValue";
    private static final String SLIDER_SMALL_INCREMENT = "sliderSmallIncrement";
    private static final String SLIDER_SMALL_DECREMENT = "sliderSmallDecrement";
    private static final String SLIDER_LARGE_INCREMENT = "sliderLargeIncrement";
    private static final String SLIDER_LARGE_DECREMENT = "sliderLargeDecrement";
    public static final String THUMB_XPATH = "./Thumb";

    public Slider(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }
    /**
     * @return The minimum value.
     */
    public double minimum() {
        var response = callVoidCommand(SLIDER_MINIMUM);
        return Double.parseDouble(response.getValue().toString());
    }

    /**
     * @return The maximum value.
     */
    public double maximum() {
        var response = callVoidCommand(SLIDER_MAXIMUM);
        return Double.parseDouble(response.getValue().toString());
    }

    /**
     * @return The value of a small change.
     */
    public double smallChange() {
        var response = callVoidCommand(SLIDER_SMALL_CHANGE);
        return Double.parseDouble(response.getValue().toString());
    }

    /**
     * @return The value of a large change.
     */
    public double largeChange() {
        var response = callVoidCommand(SLIDER_LARGE_CHANGE);
        return Double.parseDouble(response.getValue().toString());
    }

    /**
     * @return The button element used to perform a large increment.
     */
    public Button getLargeIncreaseButton() {
        var response = callVoidCommand(SLIDER_GET_LARGE_INCREASE_BUTTON);
        if (response == null) {
            return null;
        }
        return WebElementExtensions.to(createWebElementFromResponse(response));
    }

    /**
     * @return The button element used to perform a large decrement.
     */
    public Button getLargeDecreaseButton() {
        var response = callVoidCommand(SLIDER_GET_LARGE_DECREASE_BUTTON);
        if (response == null) {
            return null;
        }
        return WebElementExtensions.to(createWebElementFromResponse(response));
    }

    /**
     * @return The element used to drag.
     */
    public Thumb getThumb() {
        return WebElementExtensions.to(getSelenideElement().$x(THUMB_XPATH));
    }
    /**
     * Flag which indicates if the "Slider" supports range values (min-max) or only values (0-100).
     * Only values are for example used when combining UIA3 and WinForms applications.
     *
     * @return whether the slider supports range values or only values
     */
    public boolean isOnlyValue() {
        var response = callVoidCommand(SLIDER_IS_ONLY_VALUE);
        return Boolean.parseBoolean(response.getValue().toString());
    }

    /**
     * Gets the current value.
     *
     * @return current value of the slider
     */
    public double getValue() {
        var response = getSelenideElement().getText();
        return Double.parseDouble(response);
    }

    /**
     * Sets the current value.
     *
     * @param value specified value that should be set to the silder
     */
    public void setValue(final double value) {
        callValueCommand(SLIDER_SET_VALUE, String.valueOf(value));
    }

    /**
     * Performs a small increment.
     */
    public void smallIncrement() {
        callVoidCommand(SLIDER_SMALL_INCREMENT);
    }

    /**
     * Performs a small decrement.
     */
    public void smallDecrement() {
        callVoidCommand(SLIDER_SMALL_DECREMENT);
    }

    /**
     * Performs a large increment.
     */
    public void largeIncrement() {
        callVoidCommand(SLIDER_LARGE_INCREMENT);
    }

    /**
     * Performs a large decrement.
     */
    public void largeDecrement() {
        callVoidCommand(SLIDER_LARGE_DECREMENT);
    }
}
