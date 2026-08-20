package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class TitleBar extends TypifiedDesktopElement {

    private static final String TITLE_BAR_MINIMIZE_BUTTON = "titleBarMinimizeButton";
    private static final String TITLE_BAR_MAXIMIZE_BUTTON = "titleBarMaximizeButton";
    private static final String TITLE_BAR_RESTORE_BUTTON = "titleBarRestoreButton";
    private static final String TITLE_BAR_CLOSE_BUTTON = "titleBarCloseButton";

    public TitleBar(final WebElement wrappedElement, final String title) {
        super(wrappedElement, title);
    }

    /**
     * Gets the minimize button element.
     *
     * @return minimize button
     */
    public Button minimizeButton() {
        var response = callVoidCommand(TITLE_BAR_MINIMIZE_BUTTON);
        if (response == null) {
            return null;
        }
        return WebElementExtensions.to(createWebElementFromResponse(response));
    }

    /**
     * Gets the maximize button element.
     *
     * @return maximize button
     */
    public Button maximizeButton() {
        var response = callVoidCommand(TITLE_BAR_MAXIMIZE_BUTTON);
        if (response == null) {
            return null;
        }
        return WebElementExtensions.to(createWebElementFromResponse(response));
    }

    /**
     * Gets the restore button element.
     *
     * @return restore button
     */
    public Button restoreButton() {
        var response = callVoidCommand(TITLE_BAR_RESTORE_BUTTON);
        if (response == null) {
            return null;
        }
        return WebElementExtensions.to(createWebElementFromResponse(response));
    }

    /**
     * Gets the close button element.
     *
     * @return close button
     */
    public Button closeButton() {
        var response = callVoidCommand(TITLE_BAR_CLOSE_BUTTON);
        if (response == null) {
            return null;
        }
        return WebElementExtensions.to(createWebElementFromResponse(response));
    }
}
