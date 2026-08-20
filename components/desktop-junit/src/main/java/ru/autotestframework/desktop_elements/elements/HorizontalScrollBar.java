package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;

public class HorizontalScrollBar extends ScrollBarBase {

    private static final String HORIZONTAL_SCROLL_BAR_SCROLL_LEFT = "horizontalScrollBarScrollLeft";
    private static final String HORIZONTAL_SCROLL_BAR_SCROLL_RIGHT = "horizontalScrollBarScrollRight";
    private static final String HORIZONTAL_SCROLL_BAR_SCROLL_LEFT_LARGE = "horizontalScrollBarScrollLeftLarge";
    private static final String HORIZONTAL_SCROLL_BAR_SCROLL_RIGHT_LARGE = "horizontalScrollBarScrollRightLarge";

    public HorizontalScrollBar(final WebElement wrappedElement) {
        super(wrappedElement);
    }

    public HorizontalScrollBar(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * Scrolls left by a small amount.
     */
    public void scrollLeft() {
        callVoidCommand(HORIZONTAL_SCROLL_BAR_SCROLL_LEFT);
    }

    /**
     * Scrolls right by a small amount.
     */
    public void scrollRight() {
        callVoidCommand(HORIZONTAL_SCROLL_BAR_SCROLL_RIGHT);
    }

    /**
     * Scrolls left by a large amount.
     */
    public void scrollLeftLarge() {
        callVoidCommand(HORIZONTAL_SCROLL_BAR_SCROLL_LEFT_LARGE);
    }

    /**
     * Scrolls right by a large amount.
     */
    public void scrollRightLarge() {
        callVoidCommand(HORIZONTAL_SCROLL_BAR_SCROLL_RIGHT_LARGE);
    }
}
