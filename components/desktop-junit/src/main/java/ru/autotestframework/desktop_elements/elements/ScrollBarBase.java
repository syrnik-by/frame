package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class ScrollBarBase extends TypifiedDesktopElement {

    private static final String SCROLL_BAR_BASE_VALUE = "scrollBarBaseValue";
    private static final String SCROLL_BAR_BASE_MINIMUM_VALUE = "scrollBarBaseMinimumValue";
    private static final String SCROLL_BAR_BASE_MAXIMUM_VALUE = "scrollBarBaseMaximumValue";
    private static final String SCROLL_BAR_BASE_SMALL_CHANGE = "scrollBarBaseSmallChange";
    private static final String SCROLL_BAR_BASE_LARGE_CHANGE = "scrollBarBaseLargeChange";

    public ScrollBarBase(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    public ScrollBarBase(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * The current value of the scroll.
     *
     * @return value of the scroll
     */
    public double value() {
        var response = callVoidCommand(SCROLL_BAR_BASE_VALUE);
        return parseDouble(response);
    }

    /**
     * The minimum value of the scroll.
     *
     * @return minimum value of the scroll
     */
    public double minimumValue() {
        var response = callVoidCommand(SCROLL_BAR_BASE_MINIMUM_VALUE);
        return parseDouble(response);
    }

    /**
     * The maximum value of the scroll.
     *
     * @return maximum value of the scroll
     */
    public double maximumValue() {
        var response = callVoidCommand(SCROLL_BAR_BASE_MAXIMUM_VALUE);
        return parseDouble(response);
    }

    /**
     * The small change value of the scroll.
     *
     * @return small change value of the scroll
     */
    public double smallChange() {
        var response = callVoidCommand(SCROLL_BAR_BASE_SMALL_CHANGE);
        return parseDouble(response);
    }

    /**
     * The large change value of the scroll.
     *
     * @return large change value of the scroll
     */
    public double largeChange() {
        var response = callVoidCommand(SCROLL_BAR_BASE_LARGE_CHANGE);
        return parseDouble(response);
    }

    /**
     * Value which indicates if the scroll is read only.
     *
     * @return readonly status of the scroll
     */
    public boolean isReadOnly() {
        return isEnabled();
    }
}
