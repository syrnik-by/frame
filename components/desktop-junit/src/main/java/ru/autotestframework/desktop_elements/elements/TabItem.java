package ru.autotestframework.desktop_elements.elements;

import org.openqa.selenium.WebElement;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;

public class TabItem extends TypifiedDesktopElement {

    private static final String TAB_ITEM_SELECT = "tabItemSelect";

    public TabItem(final WebElement wrappedElement) {
        super(wrappedElement, WebElementExtensions.NO_TITLE);
    }

    /**
     * Selects the element.
     */
    public void select() {
        callVoidCommand(TAB_ITEM_SELECT);
    }

    /**
     * Adds the element to the selection.
     */
    public void addToSelection() {
        if (!getSelenideElement().isSelected()) {
            getSelenideElement().click();
        }
    }

    /**
     * Removes the element from the selection.
     */
    public void removeFromSelection() {
        if (getSelenideElement().isSelected()) {
            getSelenideElement().click();
        }
    }
}
