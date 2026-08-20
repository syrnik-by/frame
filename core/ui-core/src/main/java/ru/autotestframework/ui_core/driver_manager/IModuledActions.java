package ru.autotestframework.ui_core.driver_manager;

import org.apache.commons.lang3.NotImplementedException;
import ru.autotestframework.ui_core.typified_elements.IElement;

/**
 * Moduled actions.
 */
public interface IModuledActions {

    /**
     * Click by text.
     *
     * @param text the text
     */
    default void clickByText(String text) {
        throw new NotImplementedException();
    }

    /**
     * Drag and drop.
     *
     * @param webElementFrom the web element from
     * @param webElementTo   the web element to
     */
    default void dragAndDrop(IElement webElementFrom, IElement webElementTo) {
        throw new NotImplementedException();
    }

    /**
     * Visible multiline text.
     *
     * @param textContent the text content
     */
    default void visibleMultilineText(String textContent) {
        throw new NotImplementedException();
    }

    /**
     * Wait for element by text to disappear.
     *
     * @param seconds the seconds
     * @param text    the text
     */
    default void waitForElementByTextToDisappear(int seconds, String text) {
        throw new NotImplementedException();
    }

    /**
     * Wait for element to disappear.
     *
     * @param seconds    the seconds
     * @param webElement the web element
     */
    default void waitForElementToDisappear(int seconds, IElement webElement) {
        throw new NotImplementedException();
    }

    /**
     * Check visible text.
     *
     * @param text        the text
     * @param isDisplayed the is displayed
     */
    default void checkVisibleText(final String text, final Boolean isDisplayed) {
        throw new NotImplementedException();
    }
}
