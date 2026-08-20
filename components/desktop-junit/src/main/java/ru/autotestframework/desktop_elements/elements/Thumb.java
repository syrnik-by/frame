package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class Thumb extends TypifiedDesktopElement {

    private static final String THUMB_SLIDE_HORIZONTALLY = "thumbSlideHorizontally";
    private static final String THUMB_SLIDE_VERTICALLY = "thumbSlideVertically";

    public Thumb(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * Moves the slider horizontally.
     *
     * @param distance The distance to move the slider, + for right, - for left.
     */
    public void slideHorizontally(final int distance) {
        callValueCommand(THUMB_SLIDE_HORIZONTALLY, distance);
    }

    /**
     * Moves the slider vertically.
     *
     * @param distance The distance to move the slider, + for down, - for up.
     */
    public void slideVertically(final int distance) {
        callValueCommand(THUMB_SLIDE_VERTICALLY, distance);
    }
}
